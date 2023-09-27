package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthorrizeFilter;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.mbeans.MBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    JwtUtils utils;
    @Resource
    JwtAuthorrizeFilter jwtAuthorrizeFilter;

    @Resource
    AccountService service;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(conf->conf
                            .requestMatchers(("/api/auth/**")).permitAll()
                            .anyRequest().authenticated())
                    .formLogin(conf->conf
                            .loginProcessingUrl("/api/auth/login")
                            .failureHandler(this::onAuthenticationFailure)
                            .successHandler(this::onAuthenticationSuccess))
                    .logout(conf->conf
                            .logoutUrl("/api/auth/logout")
                            .logoutSuccessHandler(this::onLogoutSuccess))
                    .exceptionHandling(conf->conf.authenticationEntryPoint(this::onUnauthorized)
                            .accessDeniedHandler(this::onAccessDeniedhandler))
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(conf->conf
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthorrizeFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }


    public void onAccessDeniedhandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(accessDeniedException.getMessage()).asJsonString());
        System.out.println("forbidden");
    }


    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");


        User user=(User)authentication.getPrincipal();
        Account account=service.findAccountByNameOrEmail(user.getUsername());


        String token =utils.createJwt(user,account.getId(),account.getUsername());

        AuthorizeVO vo=account.asViewObject(AuthorizeVO.class,viewobject->{
            viewobject.setExpire(utils.expireTime());
            viewobject.setToken(token);
        });


//        BeanUtils.copyProperties(account,vo);
//        vo.setRole(account.getRole());
//        vo.setUsername(account.getUsername());

        response.getWriter().write(RestBean.success(vo).asJsonString());
        System.out.println(RestBean.success(token).asJsonString());
    }

    public  void onUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException  {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(authException.getMessage()).asJsonString());
        System.out.println("unauthorized");

    }
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
        System.out.println("failed");

    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization=request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)){
            writer.write(RestBean.success().asJsonString());
        }
        else {
            writer.write(RestBean.failure(400,"token失效").asJsonString());
        }

        System.out.println("logoutsuccess");
    }

}
