package p1.Server.serviceRegister;

import java.net.InetSocketAddress;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/9 0:02
 */
public interface ServiceRegister {
    //  注册：保存服务与地址。
    void register(String serviceName, InetSocketAddress serviceAddress,boolean canRetry);
}
