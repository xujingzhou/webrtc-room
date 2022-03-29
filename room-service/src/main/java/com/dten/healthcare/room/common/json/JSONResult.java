package com.dten.healthcare.room.common.json;

import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description: 自定义响应数据结构
 *                 这个类是提供给门户，ios，安卓，微信商城用的
 *                 门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 *                 其他自行处理
 *                 200：表示成功
 *                 500：表示错误，错误信息在msg字段中
 *                 501：bean验证错误，不管多少个错误都以map形式返回
 *                 502：拦截器拦截到用户token出错
 *                 555：异常抛出信息
 **/
public class JSONResult<T> {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private int code;

    // 响应消息
    private String message;

    // 响应的数据
    private T data;

    private boolean success;

    // 表中总记录数(物理分页时使用)
    @JsonIgnore
    private long total = 0;

    public JSONResult() {
    }

    public JSONResult(int code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public JSONResult(T data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
        this.success = true;
    }

    public JSONResult(T data, long total) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
        this.success = true;
        this.total = total;
    }

    public JSONResult build(int status, String message, T data, boolean success) {

        return new JSONResult<>(status, message, data, success);
    }

    public JSONResult ok(T data) {

        return new JSONResult<>(data);
    }

    public JSONResult ok(T data, long total) {

        return new JSONResult<>(data, total);
    }

    public JSONResult ok() {

        return new JSONResult<>(null);
    }

    public JSONResult errorMsg(String message) {

        return new JSONResult<>(500, message, null, false);
    }

    public JSONResult errorMap(Object data) {

        return new JSONResult<>(501, "error", data, false);
    }

    public JSONResult errorTokenMsg(String message) {

        return new JSONResult<>(502, message, null, false);
    }

    public JSONResult errorException(String message) {

        return new JSONResult<>(555, message, null, false);
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
    public void setMessage(String message) {

        this.message = message;
    }

    public Object getData() {

        return data;
    }
    public void setData(T data) {

        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTotal() {

        return total;
    }
    public void setTotal(long total) {

        this.total = total;
    }

    /**
     *
     * @Description: 将json结果集转化为JSONResult对象
     *                 需要转换的对象是一个类
     * @param jsonData
     * @param clazz
     * @return
      */
    public static JSONResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (ObjectUtil.isEmpty(clazz)) {
                return MAPPER.readValue(jsonData, JSONResult.class);
            }

            JsonNode jsonNode = MAPPER.readTree(jsonData);
            boolean success = false;
            Object obj = null;
            if (ObjectUtil.isNotEmpty(jsonNode)) {
                JsonNode data = jsonNode.get("data");
                if (ObjectUtil.isNotEmpty(clazz)) {
                    if (data.isObject()) {
                        obj = MAPPER.readValue(data.traverse(), clazz);
                    } else if (data.isTextual()) {
                        obj = MAPPER.readValue(data.asText(), clazz);
                    }

                    if (ObjectUtil.isNotEmpty(obj)) {
                        success = true;
                    }
                }
            }

            return new JSONResult<>(jsonNode.get("code").intValue(), jsonNode.get("message").asText(), obj, success);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @Description: 没有object对象的转化
     * @param json
     * @return
     */
    public static JSONResult format(String json) {
        try {
            return MAPPER.readValue(json, JSONResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @Description: Object是集合转化
     *                 需要转换的对象是一个list
     * @param jsonData
     * @param clazz
     * @return
     */
    public static JSONResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            boolean success = false;
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));

                if (ObjectUtil.isNotEmpty(obj)) {
                    success = true;
                }
            }

            return new JSONResult<>(jsonNode.get("code").intValue(), jsonNode.get("message").asText(), obj, success);
        } catch (Exception e) {
            return null;
        }
    }
}