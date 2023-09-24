package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

//读取配置文件中的属性
//@ConfigurationProperties(prefix = "spring.security.jwt")
@Component
public class JwtUtils {
//    @Value("${spring.security.jwt.key}")
//Jwt秘钥
    private  final String key="abcdefghijk";
//    @Value("${spring.security.jwt.expire}")
    private   final int   expire=7;
    //由于USERDetails中只有username和password，所以id和昵称等信息需要额外传入

    public DecodedJWT resolveJwt(String headerToken) {
        String token=this.convertToken(headerToken);
        if (token==null){
            return null;
        }
        Algorithm algorithm=Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        try {
            DecodedJWT vertify=jwtVerifier.verify(token);
            Date expriresAt=vertify.getExpiresAt();
            return new Date().after(expriresAt) ? null:vertify;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  String createJwt(UserDetails details,int id,String username){
        //加密
        Algorithm algorithm=Algorithm.HMAC256(key);

        return JWT.create()
            .withClaim("id",id)
            .withClaim("name",username)
            .withClaim("authority",details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .withExpiresAt(expireTime())
                .withIssuedAt(new Date())
                .sign(algorithm);
    }
//计算过期时间
public Date expireTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.HOUR,expire*24);
        return calendar.getTime();
    }
public Integer toId(DecodedJWT jwt){
        Map<String, Claim> claims=jwt.getClaims();
        return claims.get("id").asInt();
}
public UserDetails toUser(DecodedJWT jwt){
    Map<String, Claim> claims=jwt.getClaims();
    return User.withUsername(claims.get("name").asString())
            .password("******")
            .authorities(claims.get("authority").asArray(String.class))
            .build();
}
    private String convertToken(String headerToken){
        if(headerToken==null||!headerToken.startsWith("Bearer ")){
            return null;
        }
       return headerToken.substring(7);
    }
}
