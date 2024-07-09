package p1.Client.serviceCenter.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import p1.Client.cache.serviceCache;
import p1.Client.serviceCenter.ServiceCenter;
import p1.Client.serviceCenter.ZKWatcher.watchZK;
import p1.Client.serviceCenter.balance.impl.ConsistencyHashBalance;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/8 23:53
 */
public class ZKServiceCenter implements ServiceCenter {

    private serviceCache cache ;
    // curator 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";

    public ZKServiceCenter() throws InterruptedException {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper 连接成功");
        //初始化本地缓存
        cache=new serviceCache();
        //加入zookeeper事件监听器
        watchZK watcher=new watchZK(client,cache);
        //监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先找本地缓存
            List<String> serviceList=cache.getServcieFromCache(serviceName);
            //如果找不到，再去zookeeper中找
            //这种i情况基本不会发生，或者说只会出现在初始化阶段
            if(serviceList==null) {
                serviceList=client.getChildren().forPath("/" + serviceName);
            }
//            // 这里默认用的第一个，后面加负载均衡
//            String string = serviceList.get(0);
            // 加入负载均衡层
            String balance = new ConsistencyHashBalance().balance(serviceList);
            return parseAddress(balance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }

    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }
}
