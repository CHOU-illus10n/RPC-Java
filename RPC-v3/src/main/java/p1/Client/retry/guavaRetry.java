package p1.Client.retry;

import com.github.rholder.retry.*;
import p1.Client.client.RPCClient;
import p1.common.message.RPCRequest;
import p1.common.message.RPCResponse;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/10 1:01
 */
public class guavaRetry {
    private RPCClient rpcClient;

    public RPCResponse sendServiceWithRetry(RPCRequest request, RPCClient rpcClient){
        this.rpcClient = rpcClient;
        Retryer<RPCResponse> retryer = RetryerBuilder.<RPCResponse>newBuilder()
                .retryIfException()
                .retryIfResult(response -> Objects.equals(response.getCode(), 500))
                //重试等待策略：等待 2s 后再进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //重试停止策略：重试达到 3 次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
            @Override
            public <V> void onRetry(Attempt<V> attempt) {
                System.out.println("RetryListener: 第" + attempt.getAttemptNumber() + "次调用");
            }
        })
                .build();
        try {
            return retryer.call(() -> rpcClient.sendRequest(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RPCResponse.fail();
    }
}
