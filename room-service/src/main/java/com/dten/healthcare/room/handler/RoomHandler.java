package com.dten.healthcare.room.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.dten.healthcare.room.bean.*;
import com.dten.healthcare.room.common.EventData;
import com.dten.healthcare.room.common.RoomContext;
import com.dten.healthcare.room.common.SocketIOSend;
import com.dten.healthcare.room.common.constant.ClientEvent;
import com.dten.healthcare.room.common.constant.HandshakeParams;
import com.dten.healthcare.room.common.constant.RoomConst;
import com.dten.healthcare.room.common.constant.UserTypeEnum;
import static com.dten.healthcare.room.common.RoomContext.*;

import com.dten.healthcare.room.listener.HandlerModuleListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/30
 * @Description:
 */
@Api("普通房间管理")
@Slf4j
@Service
public class RoomHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    @Autowired
    private final SocketIOServer server;

    @Autowired
    public RoomHandler(SocketIOServer server) {

        this.server = server;

        // Add listener
        HandlerModuleListener.getInstance().addMasterListener(this);
    }

    // --------------------- 普通房间管理相关事件 ------------------------
    @ApiOperation(value = "disconnect-with", notes = "与指定节点断开")
    @OnEvent(value = "disconnect-with")
    public void disconnectWith(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("disconnect-with - remoteUserId = {}", data);

        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            String remoteUserId = jsonObject.getString("remoteUserId");
            if (StrUtil.isEmpty(remoteUserId)) {
                logger.info("disconnect-with: remoteUserId is empty.");
                return;
            }

            String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
            EventData sendParams = new EventData();
            if (ObjectUtil.isNotEmpty(usersMap.get(userId))
                    && usersMap.get(userId).connectedWith.contains(remoteUserId)) {

                usersMap.get(userId).connectedWith.remove(remoteUserId);

                sendParams.setEventName(ClientEvent.USER_DISCONNECTED);
                Map<String, Object> mapDisconnected = new HashMap<>();
                mapDisconnected.put("remoteUserId", remoteUserId);
                mapDisconnected.put("timestamp", System.currentTimeMillis());
                sendParams.setData(mapDisconnected);
                SocketIOSend.sendMessage(userId, ClientEvent.USER_DISCONNECTED, JSON.toJSONString(sendParams), UserTypeEnum.USER);
            }

            if (ObjectUtil.isNotEmpty(usersMap.get(remoteUserId))
                    && usersMap.get(remoteUserId).connectedWith.contains(userId)) {

                usersMap.get(remoteUserId).connectedWith.remove(userId);

                sendParams.setEventName(ClientEvent.USER_DISCONNECTED);
                Map<String, Object> mapDisconnected = new HashMap<>();
                mapDisconnected.put("remoteUserId", userId);
                mapDisconnected.put("timestamp", System.currentTimeMillis());
                sendParams.setData(mapDisconnected);
                SocketIOSend.sendMessage(remoteUserId, ClientEvent.USER_DISCONNECTED, JSON.toJSONString(sendParams), UserTypeEnum.USER);
            }
        } catch (Exception e) {
            logger.info("disconnect-with exception = {}", e.toString());
        }
    }

    @ApiOperation(value = "check-presence", notes = "检查房间是否存在")
    @OnEvent(value = "check-presence")
    public void checkPresence(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("check-presence - roomId = {}", data);

        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            String roomId = jsonObject.getString("roomid");
            if (StrUtil.isEmpty(roomId)) {
                logger.info("checkPresence: roomId is empty.");
                return;
            }

            if (ObjectUtil.isEmpty(roomsMap.get(roomId)) ||
                    roomsMap.get(roomId).participants.size() < 1) {
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(false, roomId);
                }
            } else {
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(true, roomId);
                }
            }
        } catch (Exception e) {
            logger.info("check-presence exception = {}", e.toString());
        }
    }

    @ApiOperation(value = "open-room", notes = "创建房间")
    @OnEvent(value = "open-room")
    public void openRoom(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("openRoom - data = {}", data);

        try {
            String roomID = StrUtil.isNotEmpty(client.get(RoomConst.ROOM_ID)) ?client.get(RoomConst.ROOM_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.SESSION_ID);
            String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);

            if (StrUtil.isNotEmpty(data)) {
                JSONObject jsonObject = JSONObject.parseObject(data);
                MessageInnerDTO message = JSONObject.toJavaObject(jsonObject, MessageInnerDTO.class);

                if (ObjectUtil.isNotEmpty(roomsMap.get(message.getSessionId()))
                    && roomsMap.get(message.getSessionId()).participants.size() > 0) {

                    if (ackRequest.isAckRequested()) {
                        ackRequest.sendAckData(false, RoomConst.ROOM_NOT_AVAILABLE);
                    }

                    logger.info("openRoom - RoomConst.ROOM_NOT_AVAILABLE. userId = {}, roomID = {}", userId, roomID);
                    return;
                }

                if (ENABLE_BROADCAST) {
                    message.getSession().setScalable(true);
                    message.setSessionId(message.getExtra().getBroadcastId());
                }

                if (ObjectUtil.isEmpty(usersMap.get(userId))) {

                    User user = new User();
                    user.setUserId(userId);
                    user.setUserName("");
                    user.setClient(client);
                    user.setSocketMessageEvent("");
                    user.setSocketCustomEvent("");
                    user.setExtra(new ExtraDTO());
                    usersMap.put(userId, user);
                }
                usersMap.get(userId).setExtra(message.getExtra());

                if (ObjectUtil.isNotEmpty(message.getSession())
                    && (message.getSession().isOneway() || message.getSession().isBroadcast())) {
                    AUTO_CLOSE_SESSION = true;
                }

                if (StrUtil.isNotEmpty(message.getSessionId())) {
                    roomID = message.getSessionId();
                }

                appendToRoom(client, roomID, userId);

                // socket本地变量
                if (StrUtil.isNotEmpty(message.getUserId()))
                    client.set(RoomConst.USER_ID, message.getUserId());
                if (StrUtil.isNotEmpty(roomID))
                    client.set(RoomConst.ROOM_ID, roomID);

                if (ENABLE_BROADCAST) {
                    if (roomsMap.get(roomID).participants.size() == 1) {
                        roomsMap.get(roomID).setOwner(userId);
                        roomsMap.get(roomID).setSession(message.getSession());
                    }
                } else {
                    roomsMap.get(roomID).setOwner(userId);
                    roomsMap.get(roomID).setSession(message.getSession());
                    roomsMap.get(roomID).setExtra(message.getExtra());
                    roomsMap.get(roomID).setMaxParticipantsAllowed(MAX_HOUSE_PARTICIPANTS_ALLOWED);

                    if (StrUtil.isNotEmpty(message.getIdentifier())) {
                        roomsMap.get(roomID).setIdentifier(message.getIdentifier());
                    }
                }

                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(true);
                }
            }
        } catch (Exception e) {
            logger.info("openRoom exception = {}", e.toString());
        }
    }

    @ApiOperation(value = "join-room", notes = "加入房间")
    @OnEvent(value = "join-room")
    public void joinRoom(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("joinRoom - data = {}", data);

        try {
            String roomID = StrUtil.isNotEmpty(client.get(RoomConst.ROOM_ID)) ?client.get(RoomConst.ROOM_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.SESSION_ID);
            String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);

            if (StrUtil.isNotEmpty(data)) {
                JSONObject jsonObject = JSONObject.parseObject(data);
                MessageInnerDTO message = JSONObject.toJavaObject(jsonObject, MessageInnerDTO.class);

                if (ENABLE_BROADCAST) {
                    message.getSession().setScalable(true);
                    message.setSessionId(message.getExtra().getBroadcastId());
                }

                if (ObjectUtil.isEmpty(usersMap.get(userId))) {

                    User user = new User();
                    user.setUserId(userId);
                    user.setUserName("");
                    user.setClient(client);
                    user.setSocketMessageEvent("");
                    user.setSocketCustomEvent("");
                    user.setExtra(new ExtraDTO());
                    usersMap.put(userId, user);
                }
                usersMap.get(userId).setExtra(message.getExtra());

                if (ObjectUtil.isEmpty(roomsMap.get(message.getSessionId()))) {
                    if (ackRequest.isAckRequested()) {
                        ackRequest.sendAckData(false, RoomConst.ROOM_NOT_AVAILABLE);
                    }
                    return;
                }

                if (StrUtil.isNotEmpty(message.getSessionId())) {
                    roomID = message.getSessionId();
                }

                if (roomsMap.size() > 0 && roomsMap.get(roomID).participants.size() >= roomsMap.get(roomID).getMaxParticipantsAllowed()) {
                    if (ackRequest.isAckRequested()) {
                        ackRequest.sendAckData(false, RoomConst.ROOM_FULL);
                    }
                    return;
                }

                appendToRoom(client, roomID, userId);

                // socket本地变量
                if (StrUtil.isNotEmpty(message.getUserId()))
                    client.set(RoomConst.USER_ID, message.getUserId());
                if (StrUtil.isNotEmpty(roomID))
                    client.set(RoomConst.ROOM_ID, roomID);

                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData(true);
                }
            }

        } catch (Exception e) {
            logger.info("joinRoom exception = {}", e.toString());
        }
    }

    // 自定义事件
    @ApiOperation(value = "msgEvent", notes = "自定义事件")
    @OnEvent(value = RoomConst.MSG_EVENT)
    public void onEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        logger.info("MSG_EVENT = {}", data);

        try {
            if (StrUtil.isNotEmpty(data)) {
                //进行去转义字符
                JSONObject jsonObject = JSONObject.parseObject(data);
                MessageDTO message = JSONObject.toJavaObject(jsonObject, MessageDTO.class);

                String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
                if (StrUtil.isNotEmpty(message.getRemoteUserId())
                        && StrUtil.equals(message.getRemoteUserId(), userId) ) {
                    logger.info("onEvent: message.getRemoteUserId() is empty. userId = {}, remoteUserId = {}", userId, message.getRemoteUserId());
                    return;
                }

                String msg = jsonObject.getString("message");
                JSONObject jsonMessage = JSON.parseObject(msg);
                boolean newParticipationRequest = false;
                if (ObjectUtil.isNotEmpty(jsonMessage.getBoolean("newParticipationRequest")))
                    newParticipationRequest = jsonMessage.getBoolean("newParticipationRequest");
                if (StrUtil.isNotEmpty(message.getRemoteUserId())
                    && newParticipationRequest) {

                    if (ENABLE_BROADCAST) {
                        User user = usersMap.get(message.getRemoteUserId());
                        if (ObjectUtil.isNotEmpty(user)) {
                            EventData sendParams = new EventData();
                            sendParams.setEventName(ClientEvent.MSG_EVENT);
                            Map<String, Object> mapMsgEvent = new HashMap<>();
                            mapMsgEvent.put("data", data);
                            mapMsgEvent.put("timestamp", System.currentTimeMillis());
                            sendParams.setData(mapMsgEvent);
                            SocketIOSend.sendMessage(user.getUserId(), ClientEvent.MSG_EVENT, JSON.toJSONString(sendParams), UserTypeEnum.USER);

                            logger.info("MSG_EVENT: user isn't empty = {}, data = {}", user.getUserId(), data);
                        }

                        if (ObjectUtil.isNotEmpty(usersMap.get(userId))
                            && StrUtil.isNotEmpty(usersMap.get(userId).getExtra().getBroadcastId())) {
                            appendToRoom(client, usersMap.get(userId).getExtra().getBroadcastId(), userId);
                        }
                    } else if (ObjectUtil.isNotEmpty(roomsMap.get(message.getRemoteUserId()))) {
                        joinARoom(client, message);
                        return;
                    }
                }

                if (ObjectUtil.isEmpty(usersMap.get(message.getSender()))) {

                    User user = new User();
                    user.setUserId(message.getSender());
                    user.setUserName("");
                    user.setClient(client);
                    user.setSocketMessageEvent("");
                    user.setSocketCustomEvent("");
                    user.setExtra(new ExtraDTO());
                    usersMap.put(message.getSender(), user);
                }

                onMessageCallback(client, data);
            }

        } catch (Exception e) {
            logger.info("MSG_EVENT exception = {}", e.toString());
        }
    }

    // --------------------- 普通房间管理私有函数 ------------------------
    private void onMessageCallback(SocketIOClient client, String data) {
        logger.info("onMessageCallback = {}", data);

        try {

            JSONObject jsonObject = JSONObject.parseObject(data);
            MessageDTO message = JSONObject.toJavaObject(jsonObject, MessageDTO.class);
            String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);

            if (ObjectUtil.isEmpty(usersMap.get(message.getSender()))) {
                logger.info("onMessageCallback - ClientEvent.USER_NOT_FOUND = {}", ClientEvent.USER_NOT_FOUND);

                EventData sendParams = new EventData();
                sendParams.setEventName(ClientEvent.USER_NOT_FOUND);
                Map<String, Object> map = new HashMap<>();
                map.put("sender", message.getSender());
                map.put("timestamp", System.currentTimeMillis());
                sendParams.setData(map);
                SocketIOSend.sendMessage(userId, ClientEvent.USER_NOT_FOUND, JSON.toJSONString(sendParams), UserTypeEnum.USER);

                return;
            }

            String msg = jsonObject.getString("message");
            JSONObject jsonMessage = JSON.parseObject(msg);
            boolean userLeft = false;
            if (ObjectUtil.isNotEmpty(jsonMessage.getBoolean("userLeft")))
                userLeft = jsonMessage.getBoolean("userLeft");

            EventData sendParams = new EventData();
            if (!userLeft
                    && !usersMap.get(message.getSender()).connectedWith.contains(message.getRemoteUserId())
                    && ObjectUtil.isNotEmpty(usersMap.get(message.getRemoteUserId()))) {

                usersMap.get(message.getSender()).connectedWith.add(message.getRemoteUserId());

                sendParams.setEventName(ClientEvent.USER_CONNECTED);
                Map<String, Object> mapConnected = new HashMap<>();
                mapConnected.put("userid", message.getRemoteUserId());
                mapConnected.put("timestamp", System.currentTimeMillis());
                sendParams.setData(mapConnected);
                SocketIOSend.sendMessage(message.getSender(), ClientEvent.USER_CONNECTED, JSON.toJSONString(sendParams), UserTypeEnum.USER);

                usersMap.get(message.getRemoteUserId()).connectedWith.add(message.getSender());
                if (ObjectUtil.isNotEmpty(usersMap.get(message.getRemoteUserId()).getClient())) {
                    sendParams.setEventName(ClientEvent.USER_CONNECTED);
                    mapConnected.put("userid", message.getSender());
                    mapConnected.put("timestamp", System.currentTimeMillis());
                    sendParams.setData(mapConnected);
                    SocketIOSend.sendMessage(message.getRemoteUserId(), ClientEvent.USER_CONNECTED, JSON.toJSONString(sendParams), UserTypeEnum.USER);
                }
            }

            if (ObjectUtil.isNotEmpty(usersMap.get(message.getSender()))
                    && usersMap.get(message.getSender()).connectedWith.contains(message.getRemoteUserId())
                    && ObjectUtil.isNotEmpty(usersMap.get(userId))) {

                message.setExtra(usersMap.get(userId).getExtra());
                // Variable parameter send
                sendParams.setEventName(ClientEvent.MSG_EVENT);
                Map<String, Object> mapMsgEvent = new HashMap<>();
                mapMsgEvent.put("data", message);
                mapMsgEvent.put("timestamp", System.currentTimeMillis());
                sendParams.setData(mapMsgEvent);
                List<String> userList = usersMap.get(message.getSender()).connectedWith;
                String remoteUserID = userList.get(userList.indexOf(message.getRemoteUserId()));
                SocketIOSend.sendMessage(remoteUserID, ClientEvent.MSG_EVENT, JSON.toJSONString(sendParams, SerializerFeature.DisableCheckSpecialChar), UserTypeEnum.USER);

                logger.info("onMessageCallback.emit - userid = {}, sendEvent = {}", remoteUserID, JSON.toJSONString(sendParams));
            }

        } catch (Exception e) {
            logger.info("onMessageCallback exception = {}", e.toString());
        }
    }

    private void joinARoom(SocketIOClient client, MessageDTO message) {
        logger.info("joinARoom - message = {}", message.toString());

        String roomID = StrUtil.isNotEmpty(client.get(RoomConst.ROOM_ID)) ?client.get(RoomConst.ROOM_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.SESSION_ID);
        String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
        if (StrUtil.isEmpty(roomID)) {
            logger.info("joinARoom: roomID is empty. userId = {}", userId);
            return;
        }

        if (ObjectUtil.isEmpty(roomsMap.get(roomID))) {
            logger.info("joinARoom: roomsMap.get(roomID) is empty. userId = {}", userId);
            return;
        }

        try {
            if (roomsMap.get(roomID).participants.size() >= roomsMap.get(roomID).getMaxParticipantsAllowed()
                    && !roomsMap.get(roomID).participants.contains(userId)) {
                logger.info("joinARoom: roomsMap Over the maximum. roomID = {}", roomID);
                return;
            }

            EventData sendParams = new EventData();
            if (ObjectUtil.isNotEmpty(roomsMap.get(roomID).getSession())
                    && (roomsMap.get(roomID).getSession().isOneway()
                    || roomsMap.get(roomID).getSession().isBroadcast())) {

                String ownerID = roomsMap.get(roomID).getOwner();
                if (ObjectUtil.isNotEmpty(usersMap.get(ownerID))) {
                    message.setRemoteUserId(ownerID);

                    if (!ENABLE_BROADCAST) {
                        sendParams.setEventName(ClientEvent.MSG_EVENT);
                        Map<String, Object> mapMsgEvent = new HashMap<>();
                        mapMsgEvent.put("data", message);
                        mapMsgEvent.put("timestamp", System.currentTimeMillis());
                        sendParams.setData(mapMsgEvent);
                        SocketIOSend.sendMessage(ownerID, ClientEvent.MSG_EVENT, JSON.toJSONString(sendParams), UserTypeEnum.USER);
                    }
                }

                return;
            }

            if (!ENABLE_BROADCAST) {
                // connect with all participants
                roomsMap.get(roomID).participants.forEach((uid) -> {
                    if (StrUtil.equals(uid, userId) || ObjectUtil.isEmpty(usersMap.get(uid))) {
                        logger.info("joinARoom: There is no User among the attendees of roomsMap. userId = {}, roomID = {}", userId, roomID);
                        return;
                    }

                    User user = usersMap.get(uid);
                    message.setRemoteUserId(uid);
                    sendParams.setEventName(ClientEvent.MSG_EVENT);
                    Map<String, Object> mapMsgEvent = new HashMap<>();
                    mapMsgEvent.put("data", message);
                    mapMsgEvent.put("timestamp", System.currentTimeMillis());
                    sendParams.setData(mapMsgEvent);
                    SocketIOSend.sendMessage(user.getUserId(), ClientEvent.MSG_EVENT, JSON.toJSONString(sendParams), UserTypeEnum.USER);
                });
            }

        } catch (Exception e) {
            logger.info("joinARoom exception = {}", e.toString());
        }
    }

    private void appendToRoom(SocketIOClient client, String roomId, String userId) {
        logger.info("appendToRoom - roomId = {}, userid = {}", roomId, userId);

        if (StrUtil.isEmpty(roomId) || StrUtil.isEmpty(userId)) {
            logger.info("joinARoom: StrUtil.isEmpty(roomId) || StrUtil.isEmpty(userId). userId = {}, roomID = {}", userId, roomId);
            return;
        }

        try {
            if (ObjectUtil.isEmpty(roomsMap.get(roomId))) {
                Room room = new Room();
                room.setRoomId(roomId);
                room.setRoomName("");
                room.setMaxParticipantsAllowed(MAX_HOUSE_PARTICIPANTS_ALLOWED);
                room.setOwner(userId);
                room.participants.add(userId);
                room.setExtra(new ExtraDTO());
                room.setIdentifier("");
                TypeOfStreamsDTO session = new TypeOfStreamsDTO();
                session.setAudio(true);
                session.setVideo(true);
                room.setSession(session);
                roomsMap.put(roomId, room);
            }

            if (roomsMap.get(roomId).participants.contains(userId))
                return;

            roomsMap.get(roomId).participants.add(userId);
        } catch (Exception e) {
            logger.info("appendToRoom exception = {}", e.toString());
        }
    }

    private List<String> getParamsByClient(SocketIOClient client, String name) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get(name);
        if (list != null && list.size() > 0) {
            return list;
        }

        return null;
    }

    // --------------------- override by IHandler ------------------------
    @Override
    public void connect(SocketIOClient client) {
        logger.info("RoomHandler: connect = {}", client.getSessionId().toString());
    }

    @Override
    public void disconnect(SocketIOClient client) {
        logger.info("RoomHandler: disconnect = {}", client.getSessionId().toString());
    }

    @Override
    public void closeOrShiftRoom(SocketIOClient client) {
        logger.info("RoomHandler: closeOrShiftRoom = {}", client.getSessionId().toString());

        try {
            String roomID = StrUtil.isNotEmpty(client.get(RoomConst.ROOM_ID)) ?client.get(RoomConst.ROOM_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.SESSION_ID);
            String userId = StrUtil.isNotEmpty(client.get(RoomConst.USER_ID)) ?client.get(RoomConst.USER_ID) :client.getHandshakeData().getSingleUrlParam(HandshakeParams.USER_ID);
            if (ObjectUtil.isEmpty(roomID) || StrUtil.isEmpty(userId)) {
                logger.info("RoomHandler - closeOrShiftRoom: roomID or userId is empty.");
                return;
            }

            if (ObjectUtil.isNotEmpty(RoomContext.roomsMap.get(roomID))) {
                if (StrUtil.equals(userId, RoomContext.roomsMap.get(roomID).getOwner())) {
                    if (!RoomContext.AUTO_CLOSE_SESSION && RoomContext.roomsMap.get(roomID).participants.size() > 1) {
                        // Shift room owner
                        AtomicReference<User> firstParticipant = new AtomicReference<>(null);
                        roomsMap.get(roomID).participants.forEach((uid) -> {
                            if (ObjectUtil.isNotEmpty(firstParticipant.get()) || StrUtil.equals(uid, userId))
                                return;

                            if (ObjectUtil.isEmpty(usersMap.get(uid)))
                                return;

                            firstParticipant.set(usersMap.get(uid));
                        });

                        if (ObjectUtil.isNotEmpty(firstParticipant.get())) {
                            roomsMap.get(roomID).setOwner(firstParticipant.get().getUserId());
                            EventData sendParams = new EventData();
                            sendParams.setEventName(ClientEvent.SET_IS_INITIATOR_TRUE);
                            Map<String, Object> map = new HashMap<>();
                            map.put("sessionid", roomID);
                            map.put("timestamp", System.currentTimeMillis());
                            sendParams.setData(map);
                            SocketIOSend.sendMessage(firstParticipant.get().getUserId(), ClientEvent.SET_IS_INITIATOR_TRUE, JSON.toJSONString(sendParams), UserTypeEnum.USER);

                            // remove from room's participants list
                            CopyOnWriteArrayList<String> newParticipantsList = new CopyOnWriteArrayList<>();
                            RoomContext.roomsMap.get(roomID).participants.forEach((uid) -> {
                                if (StrUtil.isNotEmpty(uid) && !StrUtil.equals(uid, userId)
                                        && ObjectUtil.isNotEmpty(RoomContext.usersMap.get(uid))) {
                                    newParticipantsList.add(uid);
                                }
                            });

                            if (ObjectUtil.isNotEmpty(RoomContext.roomsMap.get(roomID))) {
                                RoomContext.roomsMap.get(roomID).participants = newParticipantsList;
                            }
                        } else {
                            // remove room
                            RoomContext.roomsMap.remove(roomID);
                        }

                    } else {
                        // remove room
                        RoomContext.roomsMap.remove(roomID);
                    }
                } else {
                    CopyOnWriteArrayList<String> newParticipantsList = new CopyOnWriteArrayList<>();
                    RoomContext.roomsMap.get(roomID).participants.forEach((uid) -> {
                        if (StrUtil.isNotEmpty(uid) && !StrUtil.equals(uid, userId)
                                && ObjectUtil.isNotEmpty(RoomContext.usersMap.get(uid))) {
                            newParticipantsList.add(uid);
                        }
                    });

                    if (ObjectUtil.isNotEmpty(RoomContext.roomsMap.get(roomID))) {
                        RoomContext.roomsMap.get(roomID).participants = newParticipantsList;
                    }
                }
            }
        } catch (Exception e) {
            logger.info("RoomHandler closeOrShiftRoom exception = {}", e.toString());
        }
    }
}
