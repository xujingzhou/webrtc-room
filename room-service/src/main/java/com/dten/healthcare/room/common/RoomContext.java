package com.dten.healthcare.room.common;

import com.dten.healthcare.room.bean.Room;
import com.dten.healthcare.room.bean.User;
import com.dten.healthcare.room.common.constant.RoomConst;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
public class RoomContext {
    // 连接数(仅供内部统计使用)
    public static AtomicInteger numberOfUsers = new AtomicInteger(0);

    public static ConcurrentMap<String, User> usersMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, Room> roomsMap = new ConcurrentHashMap<>();

    public static boolean IS_SCALABLE_BROADCAST_SOCKET = false;
    public static boolean ENABLE_BROADCAST = RoomConst.ENABLE_BROADCAST;
    public static boolean AUTO_CLOSE_SESSION = RoomConst.AUTO_CLOSE_SESSION;
    public static int MAX_RELAY_COUNT_PER_USER = RoomConst.MAX_RELAY_COUNT_PER_USER;
    public static int MAX_HOUSE_PARTICIPANTS_ALLOWED = RoomConst.MAX_HOUSE_PARTICIPANTS_ALLOWED;
    public static String MSG_EVENT = RoomConst.MSG_EVENT;
    public static String SOCKET_CUSTOM_EVENT = RoomConst.SOCKET_CUSTOM_EVENT;
}
