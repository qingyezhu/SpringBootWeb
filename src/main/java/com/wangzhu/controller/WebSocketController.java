package com.wangzhu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang.zhu on 2021-01-11 15:21.
 **/
@Controller
@RequestMapping("/api/webSocket")
public class WebSocketController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/detail/{userId}")
    public ModelAndView webSocket(@PathVariable String userId) {
        final ModelAndView modelAndView = new ModelAndView("webSocket");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/push/{cid}")
    public Map<String, Object> push(@PathVariable String cid, String message) {
        final Map<String, Object> ret = new HashMap<>();
        ret.put("cid", cid);
        ret.put("message", message);
        return ret;
    }
}
