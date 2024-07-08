package p1.Client.client;


import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 19:00
 */
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
