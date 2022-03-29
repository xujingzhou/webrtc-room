package com.dten.healthcare.room.handler;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/12
 * @Description:
 */
@Component
public interface IHandler {
    public abstract void connect(SocketIOClient client);
    public abstract void disconnect(SocketIOClient client);
    public abstract void closeOrShiftRoom(SocketIOClient client);
}
