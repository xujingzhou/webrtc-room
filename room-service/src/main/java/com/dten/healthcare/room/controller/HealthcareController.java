package com.dten.healthcare.room.controller;

import com.alibaba.fastjson.JSON;
import com.dten.healthcare.room.common.json.ResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "请求正常完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题"),
        @ApiResponse(code = 500, message = "服务器出现异常")}
)
@RestController
@RequestMapping("/v1")
public class HealthcareController {
    private static final Logger logger = LoggerFactory.getLogger(HealthcareController.class);

    @CrossOrigin
    @ApiOperation(value = "index", notes = "测试", httpMethod = "GET")
    @RequestMapping
    public String index() {
        String testStr = "Hi, Johnny!";
//        ResponseData<String> data = ResponseData.out(CodeEnum.SUCCESS, testStr);
        ResponseData<String> data = ResponseData.success(testStr);
        return JSON.toJSONString(data);
    }
}
