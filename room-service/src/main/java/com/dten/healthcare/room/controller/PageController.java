package com.dten.healthcare.room.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/7/7
 * @Description:
 */
@Controller
public class PageController {

    @CrossOrigin
    @GetMapping(value = {"/"})
    public String index(){
        return "test.html";
    }
}
