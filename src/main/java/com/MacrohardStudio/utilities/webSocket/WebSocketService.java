package com.MacrohardStudio.utilities.webSocket;

import com.MacrohardStudio.model.dro.Device_Command;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.LogTitle;
import com.MacrohardStudio.model.enums.WebSocketStatusCode;
import com.MacrohardStudio.model.enums.WebSocket_Message_Type;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@ServerEndpoint("/websocket")
@Component
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

    //当有连接接入时，会创建一个新的服务器类对象
    //而spring只会给IOC容器启动时创建的对象注入userService，连接接入时创建的对象并没有注入
    //所以使用@Autowired private IDeviceService iDeviceService会报注入失败
    private static IDeviceService iDeviceService;
    @Autowired
    public void setUserService(IDeviceService iDeviceService)
    {
        this.iDeviceService = iDeviceService;
    }

    @OnOpen
    public void onOpen(Session session)
    {
        /**
         * session.getId()：当前session会话会自动生成一个id，从0开始累加的。
         */
        log.info(LogTitle.WebSocket.toString() + " 连接建立中 ==> session_id = {}", session.getId());
        //加入 Map中。将页面的sid和session绑定或者session.getId()与session
        //onlineSessionIdClientMap.put(session.getId(), session);
        onlineSessionClientList.add(session);

        //在线数加1
        onlineSessionClientCount.incrementAndGet();
        this.session = session;
        sendToOne(session, LogTitle.WebSocket.toString() + " 与服务器连接成功");
        log.info(LogTitle.WebSocket.toString() + " 连接建立成功，当前在线数为：{} ==> 开始监听新连接：session_id = {}", onlineSessionClientCount, session.getId());
    }

    @OnClose
    public void onClose(Session session)
    {
        //onlineSessionIdClientMap.remove(session.getId());
        // 从Map中移除
        onlineSessionClientList.remove(session);

        //在线数减1
        onlineSessionClientCount.decrementAndGet();
        log.info(LogTitle.WebSocket.toString() + " 连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：session_id = {}", onlineSessionClientCount, session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {
        log.info(LogTitle.WebSocket.toString() + " <== 服务端收到客户端消息 fromSession_id = {}, message = {}", session.getId(), message);

        //先声明几个变量
        WebSocket_Message_Type webSocket_message_type = null;
        Integer device_id = null;
        Integer command = null;

        //先声明服务器的回应消息
        JSONObject responseMessage = new JSONObject();

        //解析接收到的消息
        try
        {
            //为变量赋值
            JSONObject messageJson = new JSONObject(message);
            webSocket_message_type = WebSocket_Message_Type.valueOf(messageJson.getString("message_type"));
            device_id = messageJson.optInt("device_id");
            command = messageJson.optInt("command");
        }
        catch (JSONException e)
        {
            sendToOne(session, "向服务器发送的消息不符合规范");
            log.error(LogTitle.WebSocket.toString() + " 向服务器发送的消息不符合规范");
            e.printStackTrace();
            return;
        }

        switch (webSocket_message_type)
        {
            case device_control:
            {
                ResponseCode responseCode = new ResponseCode();

                //将接收到的消息包装成实体类Device_CommandDro
                Device_CommandDro device_commandDro = new Device_CommandDro();
                Device_Command device_command = new Device_Command();
                device_command.setCommand(command);
                device_commandDro.setDevice_command(device_command);
                device_commandDro.setDevice_id(device_id);

                iDeviceService.control(device_commandDro);

                //包装好服务器回应消息并发送回客户端
                try
                {
                    responseMessage.put("message_type", WebSocket_Message_Type.device_control_response);
                    responseMessage.put("device_id", device_id);
                    responseMessage.put("code", WebSocketStatusCode.OK.getValue());
                    responseMessage.put("message", "");
                    sendToOne(session, responseMessage.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                break;
            }
            case mode_select:
            {
                //已弃用该接口
                break;
            }
            case heart_beat:
            {
                try
                {
                    responseMessage.put("message_type", WebSocket_Message_Type.heart_beat_response);
                    responseMessage.put("code", WebSocketStatusCode.OK.getValue());
                    sendToOne(session, responseMessage.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        log.error(LogTitle.WebSocket.toString() + " WebSocket发生错误，错误信息为：" + error.getMessage());
        error.printStackTrace();
    }

    public void sendToAll(String message)
    {
        if(onlineSessionClientCount.get() == 0)
        {
            log.info(LogTitle.WebSocket.toString() + " 没有会话，发送消息失败");
        }
        for (Session toSession : onlineSessionClientList)
        {
            // 排除掉自己
            /*if (!toSession.equalsIgnoreCase(session))
            {
                log.info("==> 服务端给客户端群发消息 session_id = {}, toSession_id = {}, message = {}", session.getId(), toSession.getId(), message);
                toSession.getAsyncRemote().sendText(message);
            }*/
            // 异步发送
            log.info(LogTitle.WebSocket.toString() + " ==> 服务端给客户端群发消息 toSession_id = {}, message = {}", toSession.getId(), message);
            //session.getAsyncRemote().sendText(message);
        }
    }

    public void sendToOne(Session session, String message)
    {
        // 判断List中是否存在该会话
        if (!onlineSessionClientList.contains(session))
        {
            log.error(LogTitle.WebSocket.toString() + " ==> 会话 session_id = {} 不存在", session);
            return;
        }
        // 异步发送
        log.info(LogTitle.WebSocket.toString() + " ==> 服务端给客户端发送消息 toSession_id = {}, message = {}", session.getId(), message);
        session.getAsyncRemote().sendText(message);
    }
}
