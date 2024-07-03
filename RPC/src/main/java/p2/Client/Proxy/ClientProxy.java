package p2.Client.Proxy;

import lombok.AllArgsConstructor;
import p2.Client.IOClient;
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
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    //进行动态代理封装request对象
    // 传入参数Service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();
        //数据传输
        RPCResponse response = IOClient.sendRequest(host,port,request);
        return response.getData();
    }

    public  <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
