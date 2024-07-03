package p2.Server.provider;

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

    public ServiceProvider() {
        this.interfaceProvider = new HashMap<>();
    }

    public void provideServiceInterface(Object service){
        String name = service.getClass().getName();
        Class<?>[] interfaces = service.getClass().getInterfaces();
        //一个实现类可能实现多个接口
        for(Class clazz : interfaces){
            interfaceProvider.put(clazz.getName(), service);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
