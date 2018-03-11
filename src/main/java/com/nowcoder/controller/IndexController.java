package com.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 */

@Controller

public class IndexController {
    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(){
        return "Hello world";
    }
    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value="type",defaultValue = "1",required = false) int type,
                          @RequestParam(value = "key",defaultValue  ="kk",required = false) String key){
        return String.format("Profile Group of %s, Page of %d,t:%d,k:%s",groupId,userId,type,key);
    }
    @RequestMapping(path={"/template"})
    public String template(){
        return "home";
    }



}
