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
public enum IceConnectionStateEnum {
    UNKNOWN(0, "unknown"),
    NEW(1, "new"),
    CHECKING(2, "checking"),
    CONNECTED(3, "connected"),
    COMPLETED(4, "completed"),
    FAILED(5, "failed"),
    DISCONNECTED(6, "disconnected"),
    CLOSED(7, "closed");

    @JsonIgnore
    private final int  code;
    @JSONField(name = "status")
    private final String name;

    IceConnectionStateEnum(int code, String name) {
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
