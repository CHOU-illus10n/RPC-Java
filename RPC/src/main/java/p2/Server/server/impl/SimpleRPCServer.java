package p2.Server.server.impl;

import p2.Server.provider.ServiceProvider;
import p2.Server.server.RPCServer;
import p2.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zwy
 * @version 1.0
 * @description: 解析request请求并执行服务方法返回给client
 * @date 2024/7/4 1:04
 */
public class SimpleRPCServer implements RPCServer {

    // <接口服务名，service对象> 先进行简单实现，需要手写接口名称放进Map，但是我们直接用class对象自动获取即可
   // private Map<String,Object> serviceProvide;

    private ServiceProvider serviceProvider;

    public SimpleRPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                // 开启一个新线程去处理
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    @Override
    public void stop() {

    }
}
