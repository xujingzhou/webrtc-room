package com.dten.healthcare.room.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.dten.healthcare.room.bean.ExtraDTO;
import com.dten.healthcare.room.bean.User;
import com.dten.healthcare.room.common.EventData;
import com.dten.healthcare.room.common.RoomContext;
import com.dten.healthcare.room.common.constant.ClientEvent;
import com.dten.healthcare.room.common.constant.HandshakeParams;
import com.dten.healthcare.room.common.constant.RoomConst;
import com.dten.healthcare.room.listener.HandlerModuleListener;
import com.dten.healthcare.room.utils.IDUtils;
import com.dten.healthcare.room.utils.SpringContextUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/9/23
 * @Description:
 */
@Api("连接/断开管理")
@Slf4j
@Service
public class ConnectionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    @Autowired
    private final SocketIOServer server;

    @Autowired
    public ConnectionHandler(SocketIOServer server) {

        this.server = server;
    }

    // --------------------- 连接/断开相关事件 ------------------------
    @OnConnect
    public void onConnect(SocketIOClient client) {
        initWebRTCRoom(client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        clearWebRTCRoom(client);
    }

    private void initWebRTCRoom(SocketIOClient client) {
        logger.info("ConnectionHandler - initWebRTCRoom: userId = {}, socketId = {}", client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID), client.getSessionId().toString());

        // -------------- WebRTC initialization -------------- //
        String userId = client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
        String userName = client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_NAME);
        String msgEvent = client.getHandshakeData().getSingleUrlParam(HandshakeParams.MSG_EVENT);
        String socketCustomEvent = client.getHandshakeData().getSingleUrlParam(HandshakeParams.SOCKET_CUSTOM_EVENT);
        String enableBroadcast = client.getHandshakeData().getSingleUrlParam(HandshakeParams.ENABLE_BROADCAST);
        String autoCloseEntireSession = client.getHandshakeData().getSingleUrlParam(HandshakeParams.AUTO_CLOSE_ENTIRE_SESSION);
        String maxParticipantsAllowed = client.getHandshakeData().getSingleUrlParam(HandshakeParams.MAX_PARTICIPANTS_ALLOWED);
        String maxRelayCountPerUser = client.getHandshakeData().getSingleUrlParam(HandshakeParams.MAX_RELAY_COUNT_PER_USER);
        String extra = client.getHandshakeData().getSingleUrlParam(HandshakeParams.EXTRA);

        if (StrUtil.isNotEmpty(userId)) {
            if (RoomContext.usersMap.size() > 0 && ObjectUtil.isNotEmpty(RoomContext.usersMap.get(userId))) {
                final int length = 10;
                EventData sendParams = new EventData();
                sendParams.setEventName(ClientEvent.USERID_ALREADY_TAKEN);
                Map<String, Object> map = new HashMap<>();
                map.put("useridAlreadyTaken", userId);
                map.put("yourNewUserId", IDUtils.createRandomID(length));
                map.put("timestamp", System.currentTimeMillis());
                sendParams.setData(map);
                client.sendEvent(ClientEvent.USERID_ALREADY_TAKEN, JSON.toJSONString(sendParams));
                logger.info("ConnectionHandler - initWebRTCRoom: sendParams = {}", JSON.toJSONString(sendParams));
                return;
            }

            // socket本地变量
            client.set(RoomConst.USER_ID, userId);

            // Customization parameter
            if (StrUtil.isNotEmpty(msgEvent)) {
                RoomContext.MSG_EVENT = msgEvent;
            } else {
                RoomContext.MSG_EVENT = RoomConst.MSG_EVENT;
            }

            if (StrUtil.isNotEmpty(socketCustomEvent)) {
                RoomContext.SOCKET_CUSTOM_EVENT = socketCustomEvent;
            } else {
                RoomContext.SOCKET_CUSTOM_EVENT = RoomConst.SOCKET_CUSTOM_EVENT;
            }

            if (StrUtil.isNotEmpty(enableBroadcast)) {
                RoomContext.ENABLE_BROADCAST = Boolean.parseBoolean(enableBroadcast);
            } else {
                RoomContext.ENABLE_BROADCAST = RoomConst.ENABLE_BROADCAST;
            }

            if (StrUtil.isNotEmpty(autoCloseEntireSession)) {
                RoomContext.AUTO_CLOSE_SESSION = Boolean.parseBoolean(autoCloseEntireSession);
            } else {
                RoomContext.AUTO_CLOSE_SESSION = RoomConst.AUTO_CLOSE_SESSION;
            }

            if (StrUtil.isNotEmpty(maxParticipantsAllowed)) {
                RoomContext.MAX_HOUSE_PARTICIPANTS_ALLOWED = Integer.parseInt(maxParticipantsAllowed);
            } else {
                RoomContext.MAX_HOUSE_PARTICIPANTS_ALLOWED = RoomConst.MAX_HOUSE_PARTICIPANTS_ALLOWED;
            }

            if (StrUtil.isNotEmpty(maxRelayCountPerUser)) {
                RoomContext.MAX_RELAY_COUNT_PER_USER = Integer.parseInt(maxRelayCountPerUser);
            } else {
                RoomContext.MAX_RELAY_COUNT_PER_USER = RoomConst.MAX_RELAY_COUNT_PER_USER;
            }

            // Add to users
            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setClient(client);
            user.setSocketMessageEvent("");
            user.setSocketCustomEvent("");
            if (StrUtil.isNotEmpty(extra)) {
                JSONObject jsonObject = JSONObject.parseObject(extra);
                ExtraDTO extraDTO = JSONObject.toJavaObject(jsonObject, ExtraDTO.class);
                user.setExtra(extraDTO);
            }

            RoomContext.usersMap.put(userId, user);
            RoomContext.numberOfUsers.addAndGet(1);

            // 调用已注册handler中的connect中函数
            List<IHandler> masterModuleList = HandlerModuleListener.getInstance().getMasterListener();
            // convert foreach to iterator/for mode (foreach mode will occasionally throw an exception: Java.Util.ConcurrentModificationException)
            Iterator<IHandler> iterator = masterModuleList.iterator();
            while (iterator.hasNext()) {
                IHandler beanClass = iterator.next();
                if (ObjectUtil.isNotEmpty(beanClass)) {
                    IHandler p2pRoom = SpringContextUtils.getBean(beanClass.getClass());
                    p2pRoom.connect(client);
                }
            }

            logger.info("ConnectionHandler - initWebRTCRoom: connect success: [userId = {}, onlineCount = {}] ", userId, RoomContext.numberOfUsers.get());
        } else {
            logger.info("ConnectionHandler - initWebRTCRoom: onConnect: userId is empty.");
        }
        // -------------- WebRTC initialization -------------- //
    }

    private void clearWebRTCRoom(SocketIOClient client) {
        logger.info("ConnectionHandler - clearWebRTCRoom: userId = {}, socketId = {}", client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID), client.getSessionId().toString());

        // -------------- WebRTC clear -------------- //
        String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
        if (StrUtil.isNotEmpty(userId) && ObjectUtil.isNotEmpty(RoomContext.usersMap.get(userId))) {
            EventData sendParams = new EventData();
            sendParams.setEventName(ClientEvent.USER_DISCONNECTED);
            Map<String, Object> mapDisconnected = new HashMap<>();
            mapDisconnected.put("remoteUserId", userId);
            mapDisconnected.put("timestamp", System.currentTimeMillis());
            sendParams.setData(mapDisconnected);

            List<String> receivers = RoomContext.usersMap.get(userId).connectedWith;
            // convert foreach to iterator/for mode (foreach mode will  occasionally throw an exception: Java.Util.ConcurrentModificationException)
            for (int i = 0; i < receivers.size(); i++) {
                String receiver = receivers.get(i);
                if (ObjectUtil.isNotEmpty(RoomContext.usersMap.get(receiver))) {
                    User connectedClient = RoomContext.usersMap.get(receiver);
                    connectedClient.getClient().sendEvent(ClientEvent.USER_DISCONNECTED, JSON.toJSONString(sendParams));

                    if (connectedClient.connectedWith.contains(userId)) {
                        connectedClient.connectedWith.remove(userId);
                    }

                    logger.info("ConnectionHandler - clearWebRTCRoom: userId = {}, connectedWith = {}, sendParams = {}", userId, receiver, JSON.toJSONString(sendParams));
                }
            }

            try {
                // 调用已注册handler中的closeOrShiftRoom中函数
                List<IHandler> masterModuleList = HandlerModuleListener.getInstance().getMasterListener();
                // convert foreach to iterator/for mode (foreach mode will occasionally throw an exception: Java.Util.ConcurrentModificationException)
                Iterator<IHandler> iterModule = masterModuleList.iterator();
                while (iterModule.hasNext()) {
                    IHandler beanClass = iterModule.next();
                    if (ObjectUtil.isNotEmpty(beanClass)) {
                        IHandler room =  SpringContextUtils.getBean(beanClass.getClass());
                        room.closeOrShiftRoom(client);
                    }
                }
            } catch (Exception e) {
                logger.info("ConnectionHandler - clearWebRTCRoom: exception = {}", e.toString());
            }

            // remove
            RoomContext.usersMap.remove(userId);
            if (RoomContext.numberOfUsers.get() > 0) {
                RoomContext.numberOfUsers.addAndGet(-1);
            }
        }

        try {
            // 调用已注册handler中的disconnect中函数
            List<IHandler> masterModuleList = HandlerModuleListener.getInstance().getMasterListener();
            // convert foreach to iterator/for mode (foreach mode will occasionally throw an exception: Java.Util.ConcurrentModificationException)
            Iterator<IHandler> iterator = masterModuleList.iterator();
            while (iterator.hasNext()) {
                IHandler beanClass = iterator.next();
                if (ObjectUtil.isNotEmpty(beanClass)) {
                    IHandler p2pRoom = SpringContextUtils.getBean(beanClass.getClass());
                    p2pRoom.disconnect(client);
                }
            }
        } catch (Exception e) {
            logger.info("ConnectionHandler - clearWebRTCRoom: exception = {}", e.toString());
        }

        // clear
        client.set(RoomConst.USER_ID, "");
        client.set(RoomConst.ROOM_ID, "");

        logger.info("ConnectionHandler - clearWebRTCRoom: success: [userId = {}, onlineCount = {}]", userId, RoomContext.numberOfUsers.get());
        // -------------- WebRTC clear -------------- //
    }
}
