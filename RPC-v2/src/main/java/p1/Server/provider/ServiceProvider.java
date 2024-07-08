package p1.Server.provider;

import p1.Server.serviceRegister.ServiceRegister;
import p1.Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zwy
 * @version 1.0
 * @description: 存放服务接口名与服务端对应的实现类
 * @date 2024/7/4 1:24
 */
public class ServiceProvider {
    private Map<String,Object> interfaceProvider;
    private int port;
    private String host;
    //注册服务类
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }

    public void provideServiceInterface(Object service){
        String name = service.getClass().getName();
        Class<?>[] interfaces = service.getClass().getInterfaces();
        //一个实现类可能实现多个接口
        for(Class clazz : interfaces){
            interfaceProvider.put(clazz.getName(), service);
            //在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
