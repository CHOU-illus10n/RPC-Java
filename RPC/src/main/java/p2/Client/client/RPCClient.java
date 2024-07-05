package p2.Client.client;

import p2.common.message.RPCRequest;
import p2.common.message.RPCResponse;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 19:00
 */
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
