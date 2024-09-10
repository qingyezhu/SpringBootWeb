package com.wangzhu.controller;

import com.wangzhu.bean.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangz on 2021/6/7 19:53.
 **/
@RestController
public class UserController {

    @Autowired
    private UserComponent userComponent;

    @GetMapping("/getUser")
    public String getUserComponent(){
        return "结果：" + userComponent + ", 时间：" + System.currentTimeMillis();
    }
}
