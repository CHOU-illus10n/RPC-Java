package p1.Server.ratelimit.impl;

import p1.Server.ratelimit.RateLimit;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/11 19:58
 */
public class TokenBucketRateLimitImpl implements RateLimit {
    //令牌产生速度 /ms
    private static int RATE;
    //桶容量
    private static int CAPACITY;
    //当前桶容量
    private volatile int curCapacity;
    //时间戳
    private volatile long timeStamp = System.currentTimeMillis();

    public TokenBucketRateLimitImpl(int rate, int capacity) {
        RATE = rate;
        CAPACITY = capacity;
        curCapacity = capacity;
    }

    @Override
    public synchronized boolean getToken() {
        if(curCapacity>0){
            curCapacity--;
            return true;
        }
        long current = System.currentTimeMillis();
        if(current - timeStamp >= RATE){
            if((current-timeStamp)/RATE>=2){
                curCapacity+=(int)(current-timeStamp)/RATE-1;
            }
            if(curCapacity > CAPACITY) curCapacity = CAPACITY;
            timeStamp = current;
            return true;
        }
        return false;
    }
}
