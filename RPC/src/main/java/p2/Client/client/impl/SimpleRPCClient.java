package p2.Client.client.impl;

import lombok.AllArgsConstructor;
import p2.Client.client.RPCClient;
import p2.common.message.RPCRequest;
import p2.common.message.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 19:01
 */
@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {

    private String host;
    private int port;

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            Socket socket = new Socket(host,port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            RPCResponse response = (RPCResponse) objectInputStream.readObject();

            return response;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            return null;
        }

    }
}
