package p1.Client.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import p1.Client.client.RPCClient;
import p1.Client.netty.nettyInitializer.NettyClientInitializer;
import p1.Client.serviceCenter.ServiceCenter;
import p1.Client.serviceCenter.impl.ZKServiceCenter;
import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;

import java.net.InetSocketAddress;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/5 17:30
 */
public class NettyRPCClient implements RPCClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
//    private String host;
//    private int port;
//    public NettyRPCClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }
    private ServiceCenter serviceCenter;
    public NettyRPCClient(ServiceCenter serviceCenter) throws InterruptedException {
        this.serviceCenter=serviceCenter;
    }
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        //从注册中心获取host,post
        InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInterfaceName());
        String host = address.getHostName();
        int port = address.getPort();
        //创建一个channelFuture对象，代表这一个操作事件，sync方法表示堵塞直到connect完成
        ChannelFuture channelFuture  = null;
        try {
            //同步连接
            channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

}
