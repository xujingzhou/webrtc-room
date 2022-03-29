package com.dten.healthcare.room.common.json;

import com.dten.healthcare.room.common.constant.ReturnValueEnum;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
public class ResponseData<T> extends BaseResponse {
    private T data;

    private ResponseData() {}

    private ResponseData(ReturnValueEnum code, T data) {
        super(code);
        this.data = data;
    }

    public static <T> ResponseData<T> out(ReturnValueEnum code, T data) {
        return new ResponseData<T>(code, data);
    }

    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<T>(ReturnValueEnum.SUCCESS, data);
    }

    public static <T> ResponseData<T> fail(T data) {
        return new ResponseData<T>(ReturnValueEnum.FAIL, data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
