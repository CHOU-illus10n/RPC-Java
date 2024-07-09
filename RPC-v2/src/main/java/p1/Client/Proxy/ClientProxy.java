package p1.Client.Proxy;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import p1.Client.client.RPCClient;
import p1.Client.client.impl.NettyRPCClient;
import p1.Client.retry.guavaRetry;
import p1.Client.serviceCenter.ServiceCenter;
import p1.Client.serviceCenter.impl.ZKServiceCenter;
import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 19:41
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    // 传入不同的client(simple,netty), 即可调用公共的接口sendRequest发送请求
    private RPCClient rpcClient;
    private ServiceCenter serviceCenter;

    public ClientProxy() throws InterruptedException {
        serviceCenter=new ZKServiceCenter();
        rpcClient=new NettyRPCClient(serviceCenter);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();
        //数据传输
        RPCResponse response = null;
        // 为保持幂等性，只对白名单上的服务进行重试
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry框架进行重试操作
            response=new guavaRetry().sendServiceWithRetry(request,rpcClient);
        }else {
            //只调用一次
            response= rpcClient.sendRequest(request);
        }
        return response.getData();
    }

    /**
     * 这个<T> T 可以传入任何类型的List
     * 参数T
     *     第一个 表示是泛型
     *     第二个 表示返回的是T类型的数据
     *     第三个 限制参数类型为T
     * @param
     * @return
     */
    public  <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
