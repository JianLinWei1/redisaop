package com.eapil.facepay;

import java.lang.annotation.*;

/**
 * @auther JianLinWei
 * @date 2020-03-09 16:37
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface RedisDel {

    boolean all();
    String keyName();
}
