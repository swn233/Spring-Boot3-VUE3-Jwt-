package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.EmailRegisterVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    FlowUtils flowUtils;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate template;

    @Resource
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account=this.findAccountByNameOrEmail(username);
        if (account==null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User.withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }


    public Account findAccountByNameOrEmail(String text){
return this.query()
        .eq("username",text).or()
        .eq("email",text)
        .one();
    }

    @Override
    public String RegisterEmailVerifyCode(String type, String email, String ip) {
        synchronized(ip.intern()) {
            if (this.verifyLimit(ip)) {
                Random random = new Random();
                //确保code为六位数
                int code = random.nextInt(899999) + 100000;
                Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
                amqpTemplate.convertAndSend("mail", data);
                System.out.println("发送邮箱为："+data);

                template.opsForValue()
                        .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
                return null;
            } else {
                return "您的请求过于频繁，请稍后再试";
            }
        }
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email=vo.getEmail();
        String username=vo.getUsername();
        String code=template.opsForValue().get(Const.VERIFY_EMAIL_DATA+email);
        if (code==null){
            return "请先获取验证码";
        }
        if (!code.equals(vo.getCode())){
            return "验证码错误";
        }
        if (this.existsAccountByEmail(email)){
            return "此电子邮件已被其他用户注册";
        }
        if (this.existsAccountByUsername(vo.getUsername())){
            return "此用户名已被其他用户注册，请更新一个新的用户名";
        }
        String password= encoder.encode(vo.getPassword());
        Account account=new Account(null,username,password,email,"user",new Date());
        if (this.save(account)){
            template.delete(Const.VERIFY_EMAIL_DATA+email);
            return null;
        }else {
            return "内部错误";
        }
    }

    private boolean existsAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email",email));
    }

    private boolean existsAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username",username));
    }
    private boolean verifyLimit(String ip){
        String key= Const.VERIFY_EMAIL_LIMIT+ip;
        return flowUtils.limitOnceCheck(key,60);
  }
}
