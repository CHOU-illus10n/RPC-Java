package p2.Client;

import p2.Client.Proxy.ClientProxy;
import p2.common.pojo.User;
import p2.common.service.UserService;
import p2.common.pojo.Blog;
import p2.common.service.BlogService;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 19:47
 */
public class RPCClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9000);
        UserService proxy = clientProxy.getProxy(UserService.class);
        // 客户中添加新的测试用例
        BlogService blogService = clientProxy.getProxy(BlogService.class);

        User userByUserId = proxy.getUserByUserId(10);
        System.out.println("从服务端获取的user为"+userByUserId);

        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);

    }
}
