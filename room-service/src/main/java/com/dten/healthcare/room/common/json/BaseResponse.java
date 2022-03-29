package com.dten.healthcare.room.common.json;

import com.dten.healthcare.room.common.constant.ReturnValueEnum;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
class BaseResponse {

    private int code;
    private String message;
    private boolean success;

    protected BaseResponse() {}

    protected BaseResponse(ReturnValueEnum code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.success = code.isSuccess();
    }

    public static BaseResponse out(ReturnValueEnum code) {
        return new BaseResponse(code);
    }

    public static BaseResponse success() {
        return new BaseResponse(ReturnValueEnum.SUCCESS);
    }

    public static BaseResponse fail() {
        return new BaseResponse(ReturnValueEnum.FAIL);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
