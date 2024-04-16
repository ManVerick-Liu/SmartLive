package com.MacrohardStudio.utilities.interceptor;

import com.MacrohardStudio.model.enums.LogTitle;
import com.MacrohardStudio.utilities.jwt.JwtUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


@Slf4j
public class JwtInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // Debug
        //printRequestMessage(request);

        // 检查请求头部是否包含 WebSocket 握手所需的特殊字段
        String upgradeHeader = request.getHeader("Upgrade");
        String connectionHeader = request.getHeader("Connection");
        if ("websocket".equalsIgnoreCase(upgradeHeader) && "Upgrade".equalsIgnoreCase(connectionHeader))
        {
            // 如果是 WebSocket 握手请求，则执行相应的处理逻辑
            log.info(LogTitle.MQTT.toString() + " 检查到WebSocket握手请求，将对该握手请求进行JWT验证");
        }

        // 在正式跨域的请求前，浏览器会根据需要，发起一个“PreFlight”（也就是Option请求），用来让服务端返回允许的方法（如get、post），被跨域访问的Origin（来源，或者域），还有是否需要Credentials(认证信息） 三种场景：
        // 如果跨域的请求是Simple Request（简单请求 ），则不会触发“PreFlight”。Mozilla对于简单请求的要求是： 以下三项必须都成立：
        // 只能是Get、Head、Post方法
        // 除了浏览器自己在Http头上加的信息（如Connection、User-Agent），开发者只能加这几个：Accept、Accept-Language、Content-Type、。。。。
        // Content-Type只能取这几个值： application/x-www-form-urlencoded multipart/form-data text/plain
        // XHR对象对于HTTP跨域请求有三种：简单请求、Preflighted 请求、Preflighted 认证请求。
        // 简单请求不需要发送OPTIONS嗅探请求，但只能按发送简单的GET、HEAD或POST请求，且不能自定义HTTP Headers。
        // Preflighted 请求和认证请求，XHR会首先发送一个OPTIONS嗅探请求，然后XHR会根据OPTIONS请求返回的Access-Control-*等头信息判断是否有对指定站点的访问权限，并最终决定是否发送实际请求信息。
        // 那么我的get请求呢？ 原来，产生 OPTIONS 请求的原因是：自定义 Headers 头信息导致的。
        // 浏览器会去向 Server 端发送一个 OPTIONS 请求，看 Server 返回的 "Access-Control-Allow-Headers" 是否有自定义的 header 字段。
        // 因为我之前没有返回自定义的字段，所以，默认是不允许的，造成了客户端没办法拿到数据。
        String token = "";
        // 如果是 OPTIONS 请求，我们就让他通过，不管他
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
            // 如果不是，我们就把token拿到，用来做判断
        }else {
            token = request.getHeader("Authorization");
        }

        //验证token是否有效
        String user_id = JwtUtils.verify(token);

        if (user_id == null)
        {
            //如果token验证失败，则返回未授权状态
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

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

    private void printRequestMessage(HttpServletRequest request)
    {
        System.out.println("Incoming HTTP Request:");
        System.out.println("    URL: " + request.getRequestURL());
        System.out.println("    Method: " + request.getMethod());
        System.out.println("    Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("        " + headerName + ": " + request.getHeader(headerName));
        }
        System.out.println("    Parameters:");
        request.getParameterMap().forEach((param, values) -> {
            System.out.print("        " + param + ": ");
            for (String value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        });
        System.out.println("    Remote Address: " + request.getRemoteAddr());
        System.out.println();
    }
}
