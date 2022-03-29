package com.dten.healthcare.room.common.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/28
 * @Description:
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CacheEnum {
    UNKNOWN(0, "unknown"),
    CACHE(1, "cache"),
    ROOM_CACHE(2, "roomCache"),
    User_CACHE(3, "userCache"),
    P2P_USER_CACHE(4, "p2pUserCache"),
    ZOOM_USER_CACHE(5, "zoomUserCache"),
    ZOOM_ROOM_CACHE(6, "zoomRoomCache"),
    ZOOM_APPOINTMENT_CACHE(7, "zoomAppointmentCache"),
    ZOOM_PATIENT_CACHE(8, "zoomPatientCache");

    @JsonIgnore
    private final int  code;
    @JSONField(name = "status")
    private final String name;

    CacheEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
