package org.team4u.test;

import cn.hutool.core.lang.Dict;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.fhs.web.ext.handler.method.support.RequestBodyFastJsonDecoder;
import org.team4u.fhs.web.handler.method.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
@Controller("webSocketTest")
@ResponseView(success = "void")
public class WebSocketTestController {

    /**
     * 已连接客户端列表
     */
    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    /**
     * 当WebSocket创建连接时调用此方法,访问路径固定为/onWebSocketOpen
     */
    @RequestMapping
    public void onWebSocketOpen(WebSocketSession session) {
        sessions.add(session);
    }

    /**
     * 当WebSocket关闭连接时调用此方法,访问路径固定为/onWebSocketClose
     */
    @RequestMapping
    public void onWebSocketClose(WebSocketSession session) {
        sessions.remove(session);
    }

    /**
     * 当WebSocket收到文本消息时调用此方法,访问路径固定为/onWebSocketText
     */
    @RequestMapping
    public void onWebSocketText(@RequestParam("id") String id,
                                @RequestBody(RequestBodyFastJsonDecoder.class) TestController.User user) {
        // 为所有客户端发送消息
        for (WebSocketSession s : sessions) {
            s.write(user.name + " from " + id);
        }
    }

    /**
     * 当WebSocket收到二进制消息时调用此方法,访问路径固定为/onWebSocketBinary
     */
    @RequestMapping
    @ResponseView(success = "json")
    public Dict onWebSocketBinary(@RequestBody byte[] body) {
        // 为当前客户端回复消息
        return new Dict().set("length", body.length);
    }
}