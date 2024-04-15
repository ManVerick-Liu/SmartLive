package com.MacrohardStudio.utilities.webSocket;

import com.MacrohardStudio.model.dro.Device_CommandDro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint("/websocket")
public class WebSocketService
{
    /**
     * 静态变量，用来记录当前在线连接数，线程安全的类。
     */
    private static final AtomicInteger onlineSessionClientCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    private static final List<Session> onlineSessionClientList = new ArrayList<>();

    private Session session;

    @OnOpen
    public void onOpen(@PathParam("user_account") String user_account, Session session)
    {
        /**
         * session.getId()：当前session会话会自动生成一个id，从0开始累加的。
         */
        log.info("连接建立中 ==> session_id = {}， user_account = {}", session.getId(), user_account);
        //加入 Map中。将页面的sid和session绑定或者session.getId()与session
        //onlineSessionIdClientMap.put(session.getId(), session);
        onlineSessionClientList.add(session);

        //在线数加1
        onlineSessionClientCount.incrementAndGet();
        this.session = session;
        sendToOne(session, "与服务器连接成功");
        log.info("连接建立成功，当前在线数为：{} ==> 开始监听新连接：session_id = {}", onlineSessionClientCount, session.getId());
    }

    @OnClose
    public void onClose(Session session)
    {
        //onlineSessionIdClientMap.remove(session.getId());
        // 从Map中移除
        onlineSessionClientList.remove(session);

        //在线数减1
        onlineSessionClientCount.decrementAndGet();
        log.info("连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：session_id = {}", onlineSessionClientCount, session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {
        log.info("<== 服务端收到客户端消息 fromSession_id = {}, message = {}", session.getId(), message);

        Device_CommandDro device_commandDro = new Device_CommandDro();


    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        log.error("WebSocket发生错误，错误信息为：" + error.getMessage());
        error.printStackTrace();
    }

    public void sendToAll(String message)
    {
        for (Session toSession : onlineSessionClientList)
        {
            // 排除掉自己
            /*if (!toSession.equalsIgnoreCase(session))
            {
                log.info("==> 服务端给客户端群发消息 session_id = {}, toSession_id = {}, message = {}", session.getId(), toSession.getId(), message);
                toSession.getAsyncRemote().sendText(message);
            }*/
            // 异步发送
            log.info("==> 服务端给客户端群发消息 toSession_id = {}, message = {}", toSession.getId(), message);
            session.getAsyncRemote().sendText(message);
        }

    }

    public void sendToOne(Session session, String message)
    {
        // 判断List中是否存在该会话
        if (!onlineSessionClientList.contains(session))
        {
            log.error("==> 会话 session_id = {} 不存在", session);
            return;
        }
        // 异步发送
        log.info("==> 服务端给客户端发送消息 toSession_id = {}, message = {}", session.getId(), message);
        session.getAsyncRemote().sendText(message);
    }
}
