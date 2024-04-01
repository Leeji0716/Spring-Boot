package com.example.textboard_spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {
    @RequestMapping("/helloworld")
    @ResponseBody
    public String helloworld(){
        return "Hello world";
    }
}
