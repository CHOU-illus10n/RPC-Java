package p1.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/8 23:40
 */
public class serviceCache {

    //key: serviceName 服务名
    //value： addressList 服务提供者列表
    private static Map<String, List<String>> cache=new HashMap<>();

    public void addServiceToCache(String serviceName,String address){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+"的服务添加到本地缓存中");
        }else{
            List<String> addressList=new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
        }
    }
}
