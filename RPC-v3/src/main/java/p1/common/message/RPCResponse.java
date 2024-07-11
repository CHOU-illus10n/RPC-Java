package p1.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RPCResponse implements Serializable {
    // 状态信息
    private int code;
    private String message;
    //更新：加入传输数据的类型，以便在自定义序列化器中解析
    private Class<?> dataType;
    // 具体数据
    private Object data;

    public static RPCResponse success(Object data) {
        return RPCResponse.builder().code(200).dataType(data.getClass()).data(data).build();
    }
    public static RPCResponse fail() {
        return RPCResponse.builder().code(500).message("服务器发生错误").build();
    }
}