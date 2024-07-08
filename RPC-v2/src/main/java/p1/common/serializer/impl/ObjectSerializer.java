package p1.common.serializer.impl;



import p1.common.serializer.Serializer;

import java.io.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/8 15:12
 */
public class ObjectSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        // 利用java IO 对象 -> 字节数组
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
    // Java 原生序列化

}
