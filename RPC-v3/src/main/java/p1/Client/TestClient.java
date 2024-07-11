package p1.Client;


import p1.Client.Proxy.ClientProxy;
import p1.common.pojo.Blog;
import p1.common.pojo.User;
import p1.common.service.BlogService;
import p1.common.service.UserService;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 19:47
 */
public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        //NettyRPCClient nettyRPCClient = new NettyRPCClient("127.0.0.1", 9000);
        //NettyRPCClient nettyRPCClient = new NettyRPCClient();
        //将客户端传入代理客户端
        ClientProxy clientProxy=new ClientProxy();
        UserService proxy = clientProxy.getProxy(UserService.class);
        // 客户中添加新的测试用例
        BlogService blogService = clientProxy.getProxy(BlogService.class);

        User userByUserId = proxy.getUserByUserId(10);
        System.out.println("从服务端获取的user为"+userByUserId);

        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);

    }
}
