package com.eapil.facepay;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @auther JianLinWei
 * @date 2020-03-09 16:40
 */
@org.springframework.stereotype.Service
public class Impl  implements Service {
    @Autowired
    private  JedisClientPool clientPool;

    @Override
    @RedisCache(cacheTime = 60)
    public String getTest() {
       /* clientPool.set("test", "test");*/
        return "test";
    }
}
