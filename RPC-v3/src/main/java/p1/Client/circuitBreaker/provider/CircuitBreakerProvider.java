package p1.Client.circuitBreaker.provider;

import p1.Client.circuitBreaker.CircuitBreaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/11 20:40
 */
public class CircuitBreakerProvider {
    private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

    public CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker=circuitBreakerMap.get(serviceName);
        }else {
            circuitBreaker=new CircuitBreaker(3,0.5,10000);
        }
        return circuitBreaker;
    }
}
