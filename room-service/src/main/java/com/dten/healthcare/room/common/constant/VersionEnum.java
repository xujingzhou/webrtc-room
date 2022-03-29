package com.dten.healthcare.room.common.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/9/10
 * @Description:
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VersionEnum {
    UNKNOWN(0, "unknown"),
    V060(1, "/v060"),   // v0.6.0
    V070(2, "/v070");   // v0.7.0

    private final int code;
    private final String name;

    VersionEnum(int code, String name) {
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
