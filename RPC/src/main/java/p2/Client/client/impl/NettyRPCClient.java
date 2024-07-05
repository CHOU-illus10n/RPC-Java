package p2.Client.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import p2.Client.client.RPCClient;
import p2.Client.netty.nettyInitializer.NettyClientInitializer;
import p2.common.message.RPCRequest;
import p2.common.message.RPCResponse;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/5 17:30
 */
public class NettyRPCClient implements RPCClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;
    public NettyRPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        //创建一个channelFuture对象，代表这一个操作事件，sync方法表示堵塞直到connect完成
        ChannelFuture channelFuture  = null;
        try {
            channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();

            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

}
