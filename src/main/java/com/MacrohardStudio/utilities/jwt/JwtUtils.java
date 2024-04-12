package com.MacrohardStudio.utilities.jwt;

import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.service.interfaces.IUserService;
import com.MacrohardStudio.utilities.exception.NoneTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Slf4j
@Component
public class JwtUtils
{
    @Autowired
    private IUserService iUserService;

    private static final int expire_days = 7; // 默认令牌过期时间7天
    private static final String secret_key = "MacrohardStudio"; // 用于签名的密钥
    private static final String issuer = "MacrohardStudio"; // 口令的签发者

    /**
     * 获取token
     * @param user_id user_id
     * @return token
     */
    public static String getToken(String user_id)
    {
        Calendar expireDate = Calendar.getInstance();
        expireDate.add(Calendar.DATE, expire_days);

        JWTCreator.Builder tokenBuilder = JWT.create();
        tokenBuilder
                .withClaim("user_id", user_id)
                .withExpiresAt(expireDate.getTime())
                .withIssuer(issuer);

        return tokenBuilder.sign(Algorithm.HMAC256(secret_key));
    }

    /**
     * 验证JWT，并解析其中的信息
     * @param token JWT字符串
     * @return 解析出的用户ID，如果验证失败返回null
     */
    public static String verify(String token) throws NoneTokenException
    {
        DecodedJWT jwt;
        try
        {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret_key)).build();
            jwt = verifier.verify(token);
        } catch (SignatureVerificationException e)
        {
            log.error("无效的签名 ->", e);
            return null;
        } catch (TokenExpiredException e)
        {
            log.error("token过期 ->", e);
            return null;
        } catch (AlgorithmMismatchException e)
        {
            log.error("token算法不一致 ->", e);
            return null;
        } catch (Exception e)
        {
            log.error("token无效 ->", e);
            return null;
        }

        return jwt.getClaim("user_id").asString();
    }

}
