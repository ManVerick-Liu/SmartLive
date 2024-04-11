package com.MacrohardStudio.interceptor;

import com.MacrohardStudio.annotation.WithJWT;
import com.MacrohardStudio.annotation.WithoutJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JWTinterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 1.获取请求头当中存储字符串（令牌）
        String token = request.getHeader("jwtToken");
        // 2.如果拦截的不是方法就直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 3.把拦截的对象转换方法类型
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 4.通过反射获取当前具体调用的方法
        Method method = handlerMethod.getMethod();
        // 5.如果方法上面有IgnoreAuth注解，检查注解设置内容，如果为true，就放行
        if (method.isAnnotationPresent(WithoutJWT.class)) {
            // 获取注解对象里面的属性
            WithoutJWT passToken = method.getAnnotation(WithoutJWT.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 6.如果需要验证
        if (method.isAnnotationPresent(WithJWT.class)) {
            WithJWT passToken = method.getAnnotation(WithJWT.class);
            // 6.1 执行认证
            if (passToken.required()) {
                // 第一种情况,执行认证
//                if (token == null) {
//                    return true;
//                    //throw new RuntimeException("无token但需要认证，请重新登录");
//                }
//                // 第二种情况：token不为空，解析token里面的数据，能解析说明token是ok的。（1.考虑到数据库中去验证）
//                try {
//                    String username = JWT.decode(token).getAudience().get(0);
//                } catch (JWTDecodeException j) {
//                    throw new RuntimeException("该token无效，信息无法解析");
//                }
//                // 第三种情况：验证token真实性，先构建令牌验证器
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123456")).build();
//                try {
//                    jwtVerifier.verify(token);
//                } catch (JWTVerificationException e) {
//                    throw new RuntimeException("该token真实性无效，无法通过验证");
//                }
                return true;
            }
        }

        return false;
    }

}
