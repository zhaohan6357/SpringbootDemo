package com.chem2cs.controller;

import com.chem2cs.model.User;
import com.chem2cs.service.WendaService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import sun.nio.cs.FastCharsetProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


//@Controller

public class IndexController {
    @Autowired
    WendaService wendaService;
    private static  final Logger LOGGER=  LoggerFactory.getLogger(IndexController.class);
    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(HttpSession httpSession){
        LOGGER.info("RUNNING");
        return wendaService.getMessage(2)+"Hello world"+httpSession.getAttribute("msg");
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


        Map<String,Object> maptest=new HashMap<>();

        model.addAttribute("maptest",maptest);



        model.addAttribute("user",new User("zhao"));
        return "home";
    }

    @RequestMapping(path={"/request"})
    @ResponseBody

    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request, HttpSession session,@CookieValue("JSESSIONID") String sessionId){
        StringBuilder sb=new StringBuilder();
        sb.append("COOKIEVALUE:"+sessionId+"<br>");
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name= headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURL()+"<br>");
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                sb.append("Name："+cookie.getName()+" Value:"+cookie.getValue()+"<br>");
            }
        }
        response.addCookie(new Cookie("username","chem2cs"));
        response.addCookie(new Cookie("username2","chem2cs"));

        response.addHeader("hello","worrld");

        return sb.toString();
    }

    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})

    public RedirectView redirect(@PathVariable("code") int code,
                           HttpSession httpSession){
        httpSession.setAttribute("msg","jump from redirect");
        RedirectView redirectView=new RedirectView("/",true);
        if(code==301){
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }
    @RequestMapping(path={"/admin"})
    @ResponseBody
    public String admin(@RequestParam String key){
        if("admin".equals(key)){
            return "Hello admin";
        }
        throw new IllegalArgumentException("wrong param");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error"+e.getMessage();
    }

    @RequestMapping(path={"/test"})
    public String test1(Model model,@RequestParam(value="next",required =false) String next){
        model.addAttribute("next",next);
        System.out.println("test中next=------------"+next);
        return "test";
    }

    @RequestMapping(path={"/testreg"},method = {RequestMethod.POST})
    public String testreg(Model model,@RequestParam(value="next",required =false) String next){
        System.out.println("testreg中next=-----------------"+next);
        model.addAttribute("next",next);
        return "test2";
    }



}
