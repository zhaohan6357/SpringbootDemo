package com.nowcoder.controller;

import freemarker.template.Configuration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AppConfigurationEntry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String template(Model model){
        model.addAttribute("value1",12);
        model.addAttribute("value2","333");
        List<String> colors= Arrays.asList(new String[]{"RED","WHITE","BLACK"});
        model.addAttribute("colors",colors);
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        test t=new test(1,2);
        model.addAttribute("test",t);
        return "home";
    }



}
