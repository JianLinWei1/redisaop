package com.eapil.facepay;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface RedisCache {

    /**
     * @Description: 缓存的前缀
     * @Param:
     * @return:
     * @Author:
     * @Date: 2018/5/16
     */
    String keyName() default "";

    /**
     * @Description: 数据缓存时间单位s秒
     * @Param:  默认10分钟
     * @return:
     * @Author:
     * @Date: 2018/5/16
     */
    int cacheTime() default 0;

}
