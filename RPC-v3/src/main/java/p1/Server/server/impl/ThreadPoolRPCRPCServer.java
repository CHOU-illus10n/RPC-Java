package p1.Server.server.impl;



import p1.Server.provider.ServiceProvider;
import p1.Server.server.RPCServer;
import p1.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 2:03
 */
public class ThreadPoolRPCRPCServer implements RPCServer {

    private final ThreadPoolExecutor threadPool;

    private ServiceProvider serviceProvider;

    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider) {
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }

    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvide, int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue){

        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.serviceProvider = serviceProvide;
    }

    @Override
    public void start(int port) {
        System.out.println("服务器启动");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket, serviceProvider));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
