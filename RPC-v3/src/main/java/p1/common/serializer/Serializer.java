package p1.common.serializer;


import p1.common.serializer.impl.JsonSerializer;
import p1.common.serializer.impl.ObjectSerializer;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/8 15:05
 */
public interface Serializer {
    //对象序列化成字节数组
    byte[] serialize(Object obj);
    // 字节数组反序列化成消息，指定消息格式，根据message转化成对应的对象
    Object deserialize(byte[] bytes, int messageType);
    // 返回使用哪个序列器
    int getType();

    static Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
