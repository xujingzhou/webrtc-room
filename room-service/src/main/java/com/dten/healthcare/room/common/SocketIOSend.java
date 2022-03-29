package com.dten.healthcare.room.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.VoidAckCallback;
import com.dten.healthcare.room.bean.User;
import com.dten.healthcare.room.common.constant.UserTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/30
 * @Description:
 */
public class SocketIOSend {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOSend.class);
    private static Map<String, User> userSocketMap;
    private static final int heartbeatTimeout = 3; // seconds

    static {
        userSocketMap = RoomContext.usersMap;
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    /**
     * 发送消息 指定客户端 指定event
     * @param userId
     * @param eventName
     * @param message
     * @param userType
     */
    public static void sendMsg(String userId, String eventName, Object message, UserTypeEnum userType) {
        if (StrUtil.isNotEmpty(userId)) {
            if (userType == UserTypeEnum.USER && ObjectUtil.isNotEmpty(userSocketMap.get(userId))) {
                userSocketMap.get(userId).getClient().sendEvent(eventName, new VoidAckCallback(heartbeatTimeout) {
                    @Override
                    protected void onSuccess() {
                        log("sendMsg(USER) onSuccess");
                    }

                    public void onTimeout() {
                        log("sendMsg(USER) onTimeout");
                    }
                }, message);
            } else {
                logger.info("sendMsg: userId is empty!");
            }
        }
    }

    /**
     * 发送消息 指定客户端 指定event
     * @param userId
     * @param eventName
     * @param message
     * @param userType
     */
    public static boolean sendMessage(String userId, String eventName, Object message, UserTypeEnum userType){
        boolean result = true;
        if (StrUtil.isNotEmpty(userId)) {
            if (userType == UserTypeEnum.USER && ObjectUtil.isNotEmpty(userSocketMap.get(userId))) {
                userSocketMap.get(userId).getClient().sendEvent(eventName, message);
            } else {
                result = false;
                log("sendMessage: userId is empty!");
            }
        }

        return result;
    }

    /**
     * 发送消息 指定event 全部客户端
     * @param message
     */
    public static void sendBroardcast(String eventName, Object message, UserTypeEnum userType){
        if (userType == UserTypeEnum.USER) {
            Set<String> clientIdSet = userSocketMap.keySet();
            for (String clientId:clientIdSet) {
                userSocketMap.get(clientId).getClient().sendEvent(eventName, message);
            }
        }
    }
}
