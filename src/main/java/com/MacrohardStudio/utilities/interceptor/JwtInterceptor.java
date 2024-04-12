package com.MacrohardStudio.utilities.interceptor;

import com.MacrohardStudio.annotation.WithJWT;
import com.MacrohardStudio.annotation.WithoutJWT;
import com.MacrohardStudio.utilities.exception.ErrorFormatTokenException;
import com.MacrohardStudio.utilities.exception.NoneTokenException;
import com.MacrohardStudio.utilities.jwt.JwtUtils;
import com.MacrohardStudio.utilities.mqtt.clients.receiver.ReceiverClient;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {

        //从请求头中获取JWT token
        String token = request.getHeader("Authorization");

        //检查token是否存在并且以Bearer开头
        //在Http请求头中的Authentication是用于装载身份验证信息的字段
        //如果其中包含的信息是以Bearer开头，这将便于服务器确定该口令是JWT的口令，而不是其他类型的口令
        //这是Http请求头Authentication字段的规范
        if (token == null)
        {
            //如果token不存在或不以Bearer开头，则返回未授权状态
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("token为空");
            return false;
        }

        else if (!token.startsWith("Bearer "))
        {
            //如果token不以Bearer开头，则返回未授权状态
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("token不符合规范");
            return false;
        }

        //截取token中的实际令牌部分（略过"Bearer "）
        token = token.substring(7);

        //验证token是否有效
        String user_id = JwtUtils.verify(token);
        /*try
        {
            user_id = JwtUtils.verify(token);
        } catch (SignatureVerificationException e)
        {
            log.error("无效签名！ 错误 ->", e);
            return false;
        } catch (TokenExpiredException e)
        {
            log.error("token过期！ 错误 ->", e);
            return false;
        } catch (AlgorithmMismatchException e)
        {
            log.error("token算法不一致！ 错误 ->", e);
            return false;
        } catch (Exception e)
        {
            log.error("token无效！ 错误 ->", e);
            return false;
        }*/

        if (user_id == null)
        {
            //如果token验证失败，则返回未授权状态
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }


        //如果token验证成功，可以根据需要进行一些其他操作，例如将用户ID放入请求中以供后续处理

        //返回true表示继续执行后续的请求处理
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        //在请求处理之后调用，可以进行一些后续处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        //在请求完成后调用，可以进行一些清理工作
    }

}
