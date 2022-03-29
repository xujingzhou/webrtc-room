package com.dten.healthcare.room.common.constant;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description: 常量
 */
public class RoomConst {
	public static final String ROOM_NOT_AVAILABLE = "该房间是无效的";
	public static final String INVALID_PASSWORD = "无效的密码";
	public static final String USERID_NOT_AVAILABLE = "用户ID不存在";
	public static final String ROOM_PERMISSION_DENIED = "没有房间使用权限";
	public static final String ROOM_FULL = "房间已满";
	public static final String DID_NOT_JOIN_ANY_ROOM = "还没有加入任何房间";
	public static final String INVALID_SOCKET = "无效的Socket";
	public static final String PUBLIC_IDENTIFIER_MISSING = "无效的房间ID";
	public static final String INVALID_ADMIN_CREDENTIAL = "无效的用户名和密码";

	public static final int MAX_RELAY_COUNT_PER_USER = 3;
	public static final int MAX_HOUSE_PARTICIPANTS_ALLOWED = 100;
	public static final boolean AUTO_CLOSE_SESSION = true;
	public static final boolean ENABLE_BROADCAST = false;
	public static final String MSG_EVENT = "msgEvent";
	public static final String SOCKET_CUSTOM_EVENT = "socketCustomEvent";

	public static final String USER_ID = "userId";
	public static final String ROOM_ID = "roomId";
}
