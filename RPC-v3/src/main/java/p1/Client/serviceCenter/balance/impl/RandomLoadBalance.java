package p1.Client.serviceCenter.balance.impl;

import p1.Client.serviceCenter.balance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/9 18:39
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("负载均衡选择了"+choose+"服务器");
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
