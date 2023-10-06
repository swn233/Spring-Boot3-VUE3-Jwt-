package com.example.utils;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {
    @Resource
    StringRedisTemplate template;
    public  boolean limitOnceCheck(String key,int blockTime){
        //正在冷却的状态
        if (Boolean.TRUE.equals(template.hasKey(key))){
return false;
        }
        else {
            //如果不在冷却时间内，可以发送邮件，发挥true，并更新冷却时间
            template.opsForValue().set(key,"",blockTime, TimeUnit.SECONDS);
            return true;
        }
    }
}
