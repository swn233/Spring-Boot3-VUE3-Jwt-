package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.EmailRegisterVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account> , UserDetailsService {
    Account findAccountByNameOrEmail(String text);
    //type区分用户是注册还是更改密码，从而显示不同的文本提示;通过用户ip地址限制请求的频率
    String RegisterEmailVerifyCode(String type, String email,String ip);
    String registerEmailAccount(EmailRegisterVO vo);
}
