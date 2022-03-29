package com.dten.healthcare.room.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.dten.healthcare.room.listener.HandlerModuleListener;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/9/23
 * @Description:
 */
@Api("远程控制信令管理")
@Slf4j
@Service
public class RemoteControlHandler implements IHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SocketIOServer server;

    public RemoteControlHandler(SocketIOServer server) {
        this.server = server;

        // Add listener
        HandlerModuleListener.getInstance().addMasterListener(this);
    }

    // --------------------- override by IHandler ------------------------
    @Override
    public void connect(SocketIOClient client) {
        logger.info("RemoteControlHandler: connect start... sessionId = {}", client.getSessionId().toString());
    }

    @Override
    public void disconnect(SocketIOClient client) {
        logger.info("RemoteControlHandler: disconnect start... sessionId = {}", client.getSessionId().toString());
    }

    @Override
    public void closeOrShiftRoom(SocketIOClient client) {
        logger.info("RemoteControlHandler: closeOrShiftRoom sessionId = {}", client.getSessionId().toString());
    }
    // --------------------- override by IHandler ------------------------
}
