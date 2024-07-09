package p1.Server;

import p1.Server.provider.ServiceProvider;
import p1.Server.server.RPCServer;
import p1.Server.server.impl.NettyRPCServer;
import p1.common.service.BlogService;
import p1.common.service.UserService;
import p1.common.service.impl.BlogServiceImpl;
import p1.common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

//        Map<String, Object> serviceProvide = new HashMap<>();
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.UserService",userService);
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.BlogService",blogService);
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",9000);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        
        RPCServer RPCServer = new NettyRPCServer(serviceProvider);
        RPCServer.start(9000);
    }
}