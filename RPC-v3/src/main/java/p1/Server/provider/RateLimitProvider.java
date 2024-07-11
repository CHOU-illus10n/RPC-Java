package p1.Server.provider;

import p1.Server.ratelimit.RateLimit;
import p1.Server.ratelimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/11 20:06
 */
public class RateLimitProvider {
    private Map<String, RateLimit> rateLimitMap = new HashMap<>();

    public RateLimit getRateLimit(String interfaceName) {
        if(!rateLimitMap.containsKey(interfaceName)){
            RateLimit rateLimit=new TokenBucketRateLimitImpl(100,10);
            rateLimitMap.put(interfaceName,rateLimit);
            return rateLimit;
        }
        return rateLimitMap.get(interfaceName);
    }
}
