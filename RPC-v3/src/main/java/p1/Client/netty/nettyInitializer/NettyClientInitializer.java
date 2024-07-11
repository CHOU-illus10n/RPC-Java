package p1.Client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import p1.Client.netty.handler.NettyClientHandler;
import p1.Server.provider.ServiceProvider;
import p1.common.serializer.impl.JsonSerializer;
import p1.common.serializer.myCode.MyDecode;
import p1.common.serializer.myCode.MyEncode;


/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 20:02
 */

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
