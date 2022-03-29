package com.dten.healthcare.room.common.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/8/9
 * @Description:
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VideoSourceEnum {
    UNKNOWN(0, "unknown"),
    ZOOM(1, "zoom"),
    WEBRTC(2, "webrtc");

//    @EnumValue
    private final int code;
//    @JsonValue
    private final String name;

    VideoSourceEnum(int code, String name) {
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
