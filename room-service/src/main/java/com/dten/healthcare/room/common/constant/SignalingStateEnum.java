package com.dten.healthcare.room.common.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignalingStateEnum {
    UNKNOWN(0, "unknown"),
    STABLE(1, "stable"),
    HAVE_LOCAL_OFFER(2, "have-local-offer"),
    HAVE_REMOTE_OFFER(3, "have-remote-offer"),
    HAVE_LOCAL_PRANSWER(4, "have-local-pranswer"),
    HAVE_REMOTE_PRANSWER(5, "have-remote-pranswer"),
    CLOSED(6, "closed");

    @JsonIgnore
    private final int code;
    @JSONField(name = "status")
    private final String name;

    SignalingStateEnum(int code, String name) {
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
