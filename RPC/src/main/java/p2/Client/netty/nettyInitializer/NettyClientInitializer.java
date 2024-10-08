package p2.Client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import p2.Client.netty.handler.NettyClientHandler;
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

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//        // 计算当前待发送消息的长度，写入到前4个字节中
//        pipeline.addLast(new LengthFieldPrepender(4));

//        // 这里使用的还是java 序列化方式， netty的自带的解码编码支持传输这种结构
//        pipeline.addLast(new ObjectEncoder());
//        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
//            @Override
//            public Class<?> resolve(String className) throws ClassNotFoundException {
//                return Class.forName(className);
//            }
//        }));

        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode(new JsonSerializer()));

        pipeline.addLast(new NettyClientHandler());
    }
}
