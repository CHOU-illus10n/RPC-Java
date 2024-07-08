package p1.Server.server;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 0:47
 */
// 开放封闭原则，以后服务端实现该接口即可
public interface RPCServer {
    void start(int port);
    void stop();
}
