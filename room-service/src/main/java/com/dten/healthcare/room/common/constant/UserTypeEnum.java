package com.dten.healthcare.room.common.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/12
 * @Description:
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserTypeEnum {
    UNKNOWN(0, "unknown"),
    USER(1, "user"),
    P2P_USER(2, "p2p_user");

    private final int code;
    private final String name;

    UserTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }
    public String getStatus() {
        return name;
    }
}
