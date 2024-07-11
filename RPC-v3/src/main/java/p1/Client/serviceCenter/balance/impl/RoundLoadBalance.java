package p1.Client.serviceCenter.balance.impl;

import p1.Client.serviceCenter.balance.LoadBalance;

import java.util.List;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/9 18:42
 */
public class RoundLoadBalance implements LoadBalance {
    private int choose=-1;
    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose=choose%addressList.size();
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
