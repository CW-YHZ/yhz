package com.cw.yhz.WebSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * web socket 连接
 */
@Slf4j
@Component
@EnableWebSocket
@ServerEndpoint(value = "/wsdemo")
public class WebSocketConnect {

    /*连接数*/
    private static int onlineCount = 0;

    /*连接对象*/
    private static CopyOnWriteArraySet<WebSocketConnect> webSocketSet = new CopyOnWriteArraySet<>();

    /*session 会话*/
    private Session session;

    /**
     * 功能描述：连接成功
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中

        addOnlineCount();           //在线数加1

        log.debug("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接已建立成功.");
        } catch (Exception e) {
            log.debug("IO 异常");
        }
    }

    /**
     * 功能描述：关闭连接
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);   //连接关闭后，将此websocket从set中删除
        subOnlineCount();               //在线数减1

        log.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 功能描述：接收客户端推送的消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("来自客户端的消息:" + message);
    }

    /**
     * 功能描述：发生错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.debug("发生错误");
        error.printStackTrace();
    }

    /**
     * 功能描述：向客户端发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);

    }


    /**
     * 功能描述：获取连接数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 功能描述：连接数加 1
     * @return
     */
    public static synchronized void addOnlineCount() {
        WebSocketConnect.onlineCount++;
    }

    /**
     * 功能描述：连接数减 1
     * @return
     */
    public static synchronized void subOnlineCount() {
        WebSocketConnect.onlineCount--;
    }

    /**
     * 功能描述：获取连接对象
     * @return
     */
    public static synchronized CopyOnWriteArraySet<WebSocketConnect> getWebSocketSet() {
        return webSocketSet;
    }

}
