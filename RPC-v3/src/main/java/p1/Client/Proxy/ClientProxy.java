package p1.Client.Proxy;



import lombok.AllArgsConstructor;
import p1.Client.circuitBreaker.CircuitBreaker;
import p1.Client.circuitBreaker.provider.CircuitBreakerProvider;
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
    private CircuitBreakerProvider circuitBreakerProvider;

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
        //获取熔断器
        CircuitBreaker circuitBreaker=circuitBreakerProvider.getCircuitBreaker(method.getName());
        //判断熔断器是否允许请求经过
        if (!circuitBreaker.allowRequest()){
            //这里可以针对熔断做特殊处理，返回特殊值
            return null;
        }
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
        //记录response的状态，上报给熔断器
        if (response.getCode() ==200){
            circuitBreaker.recordSuccess();
        }
        if (response.getCode()==500){
            circuitBreaker.recordFailure();
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
