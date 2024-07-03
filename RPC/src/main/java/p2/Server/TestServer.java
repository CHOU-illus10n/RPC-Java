package p2.Server;


import p2.Server.provider.ServiceProvider;
import p2.Server.server.RPCServer;
import p2.Server.server.impl.SimpleRPCServer;
import p2.common.service.BlogService;
import p2.common.service.UserService;
import p2.common.service.impl.BlogServiceImpl;
import p2.common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

//        Map<String, Object> serviceProvide = new HashMap<>();
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.UserService",userService);
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.BlogService",blogService);
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        
        RPCServer RPCServer = new SimpleRPCServer(serviceProvider);
        RPCServer.start(9000);
    }
}