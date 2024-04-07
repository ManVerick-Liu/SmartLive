import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/webSocket")//主要是将目前的类定义成一个websocket服务器端, 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
@Component
@EnableScheduling// cron定时任务
@Data
@Slf4j
public class WebSocket
{

    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public static CopyOnWriteArraySet<WebSocket> getWebSocketSet()
    {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<WebSocket> webSocketSet) {
        WebSocket.webSocketSet = webSocketSet;
    }

    /**
     * 从数据库查询相关数据信息,可以根据实际业务场景进行修改
     */
    /*@Resource
    private IndexService indexService;
    private static IndexService indexServiceMapper;

    @PostConstruct
    public void init() {
        WebSocket.indexServiceMapper = this.indexService;
    }*/

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话
     */
    @OnOpen
    public void onOpen(Session session) throws Exception
    {
        this.session = session;
        webSocketSet.add(this);

        //查询当前在线人数
        //int nowOnline = indexServiceMapper.nowOnline();
        //this.sendMessage(JSON.toJSONString(nowOnline));

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {
        logger.info("参数信息：{}", message);
        //群发消息
        /*for (WebSocket item : webSocketSet)
        {
            try
            {
                item.sendMessage(JSON.toJSONString(message));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }*/


    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session 会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("连接异常！");
        error.printStackTrace();
    }

    /**
     * 发送信息
     *
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 自定义消息推送、可群发、单发
     *
     * @param message 消息
     */
    public static void sendInfo(String message) throws IOException
    {
        logger.info("信息:{}", message);
        for (WebSocket item : webSocketSet)
        {
            item.sendMessage(message);
        }
    }
}
