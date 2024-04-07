import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class IndexScheduled {

    @Autowired
    private IndexMapper indexMapper;

    /**
     * 每3秒执行一次
     */
    //@Scheduled(cron = "0/3 * * * * ? ") //我这里暂时不需要运行这条定时任务，所以将注解注释了，朋友们运行时记得放开注释啊
    public void nowOnline() {
        System.err.println("*********   首页定时任务执行   **************");

        CopyOnWriteArraySet<WebSocket> webSocketSet = WebSocket.getWebSocketSet();
        int nowOnline = indexMapper.nowOnline();
        webSocketSet.forEach(c -> {
            try {
                c.sendMessage(JSON.toJSONString(nowOnline));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.err.println("/n 首页定时任务完成.......");
    }

}