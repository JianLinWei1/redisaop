package com.eapil.facepay;

import com.alibaba.fastjson.JSON;
import com.eapil.facepay.JedisClientPool;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;


public class RedisCacheAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JedisClientPool jedisClientPool;


    @Around("redisCacheAspect()")
    public Object redisCache(ProceedingJoinPoint pjp) throws Throwable {
        //得到类名、方法名和参数
        String redisResult = "";
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        //根据类名，方法名和参数生成key
        String key = genKey(className,methodName,args);
        logger.info("生成的key[{}]",key);
        //得到被代理的方法
        Signature signature = pjp.getSignature();
        if(!(signature instanceof MethodSignature)){
            throw  new IllegalArgumentException();
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = pjp.getTarget().getClass().getMethod(methodSignature.getName(),methodSignature.getParameterTypes());
        //得到被代理的方法上的注解

        Object result = null;

        //判断方法上是否有加入缓存的注解
        if (method.isAnnotationPresent(RedisCache.class)){

            String keyName = method.getAnnotation(RedisCache.class).keyName();
            int cacheTime = method.getAnnotation(RedisCache.class).cacheTime();

            if (keyName != null && !keyName.equals("")){
                key = keyName+":"+key;
            }

            if(!jedisClientPool.exists(key)) {
                logger.info("缓存未命中");
                //缓存不存在，则调用原方法，并将结果放入缓存中
                result = pjp.proceed(args);
                redisResult = JSON.toJSONString(result);

                if (cacheTime== 0){
                    jedisClientPool.set(key,redisResult);
                }else {
                    jedisClientPool.set(key,redisResult,cacheTime);
                }
            } else{
                //缓存命中
                logger.info("缓存命中");
                redisResult = jedisClientPool.get(key);
                //得到被代理方法的返回值类型
                Class returnType = method.getReturnType();
                result = JSON.parseObject(redisResult,returnType);
            }
        }
        //判断方法是否有删除缓存的注解
        else if (method.isAnnotationPresent(RedisDel.class)){
            result = pjp.proceed(args);
            RedisDel redisDel = method.getAnnotation(RedisDel.class);
            //是否删除所有
            boolean all = redisDel.all();
            String s = redisDel.keyName();
            if (all){
                Set<String> keys = jedisClientPool.keys(s+"*");
                for (String s1:keys){
                    jedisClientPool.del(s1);
                }
            }else {
                jedisClientPool.del(s);
            }

        }else {
            //执行方法。返回
            result = pjp.proceed(args);
        }
        return result;
    }

    /**
     * @Description: 生成key
     * @Param:
     * @return:
     * @Author:
     * @Date: 2018/5/16
     */
    private String genKey(String className, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder("");
        sb.append(className);
        sb.append(".");
        sb.append(methodName);
        for (Object object: args) {
            logger.info("obj:"+object);
            if(object!=null) {
                sb.append("_");
                sb.append(object+"");
            }
        }
        return sb.toString();
    }






}
