package p2.Server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import p2.Server.netty.handler.NettyRPCServerHandler;
import p2.Server.provider.ServiceProvider;
import p2.common.serializer.impl.JsonSerializer;
import p2.common.serializer.myCode.MyDecode;
import p2.common.serializer.myCode.MyEncode;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/4 20:02
 */

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编/解码器
        pipeline.addLast(new MyEncode(new JsonSerializer()));
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
