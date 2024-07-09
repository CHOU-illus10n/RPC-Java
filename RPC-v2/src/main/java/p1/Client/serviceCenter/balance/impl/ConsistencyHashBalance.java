package p1.Client.serviceCenter.balance.impl;

import p1.Client.serviceCenter.balance.LoadBalance;

import java.util.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/9 18:50
 */
public class ConsistencyHashBalance implements LoadBalance {

    // 虚拟节点的个数
    private static final int VIRTUAL_NUM = 5;
    // 虚拟节点分配，key是hash值，value是虚拟节点服务器名称
    private static SortedMap<Integer, String> shards = new TreeMap<Integer, String>();
    // 真实节点列表
    private static List<String> realNodes = new LinkedList<String>();
    //模拟初始服务器
    private static String[] servers =null;

    private static void init(List<String> serviceList) {
        for(String server:serviceList){
            realNodes.add(server);
            System.out.println("真实节点"+server+"被添加");
            createVirtualNode(server);
        }
    }
    /**
     * FNV1_32_HASH算法
     */
    private static int getHash(String virtualNode) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < virtualNode.length(); i++)
            hash = (hash ^ virtualNode.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random,addressList);
    }

    /**
     * 获取分配的结点名
     * @param node
     * @param serviceList
     * @return
     */
    private String getServer(String node, List<String> serviceList) {
        init(serviceList);
        int hash = getHash(node);
        Integer key = null;
        // 其所有对象的 key 的值大于等于 hash
        // 实现向右找临近结点
        SortedMap<Integer, String> subMap  = shards.tailMap(hash);
        if (subMap.isEmpty()) {
            key = shards.lastKey();
        } else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }

    /**
     * 添加结点
     * @param node
     */
    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("真实节点[" + node + "] 上线添加");
            createVirtualNode(node);
        }
    }

    /**
     * 删除结点
     * @param node
     */

    @Override
    public void delNode(String node) {
        if(realNodes.contains(node)){
            realNodes.remove(node);
            System.out.println("真实节点[" + node + "] 下线移除");
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被移除");
            }
        }
    }

    private static void createVirtualNode(String node) {
        for (int i = 0; i < VIRTUAL_NUM; i++) {
            String virtualNode = node + "&&VN" + i;
            int hash = getHash(virtualNode);
            shards.put(hash, virtualNode);
            System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
        }
    }
}
