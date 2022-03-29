package com.dten.healthcare.room.common.constant;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/3
 * @Description:
 */
public enum ReturnValueEnum {
    SUCCESS(200, true, "success"),
    FAIL(-1, false, "error"),;

    private final int code;
    private boolean success;
    private final String message;

    ReturnValueEnum(int code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
