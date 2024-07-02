package p1.Server;

import org.apache.logging.log4j.core.jmx.Server;
import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;
import p1.common.service.Impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 19:17
 */
public class RPCServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(()-> {
                    try {
                        // 处理请求
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传过来的request
                        RPCRequest request = (RPCRequest) ois.readObject();
                        // 反射调用对应方法
                        Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
                        Object invoke = method.invoke(userService, request.getParams());
                        // 封装，写入response对象
                        oos.writeObject(RPCResponse.success(invoke));
                        oos.flush();
                    }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                            InvocationTargetException e){
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
