package com.eapil.facepay;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @auther JianLinWei
 * @date 2020-03-09 17:08
 */
@Aspect
@Component
public class MyAspect  extends  RedisCacheAspect {

    @Pointcut("execution(public  * com.eapil.facepay..*.*(..))")
    public  void  redisCacheAspect(){

    }
}
