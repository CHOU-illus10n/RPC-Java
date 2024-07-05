package p2.Client.Proxy;

import lombok.AllArgsConstructor;
import p2.Client.IOClient;
import p2.Client.client.RPCClient;
import p2.common.message.RPCRequest;
import p2.common.message.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 19:41
 */

public class ClientProxy implements InvocationHandler {
    // 传入不同的client(simple,netty), 即可调用公共的接口sendRequest发送请求
    private RPCClient rpcClient;

    public ClientProxy(RPCClient rpcClient){
        this.rpcClient = rpcClient;
    }

//    public ClientProxy(String host,int port,int choose){
//        switch (choose){
//            case 0:
//                rpcClient=new NettyRpcClient(host,port);
//                break;
//            case 1:
//                rpcClient=new SimpleSocketRpcCilent(host,port);
//        }
//    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();
        //数据传输
        RPCResponse response = rpcClient.sendRequest(request);
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
