package p1.common.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;
import p1.common.serializer.Serializer;


/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/8 18:25
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        byte bytes[] = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                RPCRequest request = JSON.parseObject(bytes, RPCRequest.class);
                Object[] objects = new Object[request.getParams().length];
                // json字符串转换为对象
                for (int i = 0; i < objects.length;i++){
                    Class<?> paramsType = request.getParamsTypes()[i];
                    if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){//检查 paramsType 是否是 request 对象的第 i 个参数的类型或其超类/超接口
                        objects[i] = JSONObject.toJavaObject((JSONObject)request.getParams()[i],request.getParamsTypes()[i]);
                    }else{
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                RPCResponse response = JSON.parseObject(bytes, RPCResponse.class);
                Class<?> dataType = response.getDataType();
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }
                obj = response;
                break;
            default:
                System.out.println("暂不支持此种消息");
                throw  new RuntimeException();

        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}


