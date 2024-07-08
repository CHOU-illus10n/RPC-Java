package p1.Client.Proxy;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import p1.Client.client.RPCClient;
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
@NoArgsConstructor
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    // 传入不同的client(simple,netty), 即可调用公共的接口sendRequest发送请求
    private RPCClient rpcClient;

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
