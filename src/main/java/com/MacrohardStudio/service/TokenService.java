package com.MacrohardStudio.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    /**
     * 假定第一次登陆成功，生成jwt字符串
     * @param username  jwt里面保存用户信息，签名密钥默认123456
     * @return
     */
    public String getToken(String username) {
        String token="";
        token= JWT.create().withAudience(username)
                .sign(Algorithm.HMAC256("123456"));
        return token;
    }
}