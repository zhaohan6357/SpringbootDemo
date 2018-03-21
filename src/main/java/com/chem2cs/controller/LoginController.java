package com.chem2cs.controller;


import com.chem2cs.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username")String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="next",required = false) String next,
                        @RequestParam(value = "rememberme",defaultValue ="false")
                                    boolean rememberme,
                        HttpServletResponse response){
        Map<String,String> map=userService.login(username,password);
        try {
            if(map.containsKey("ticket")){
                Cookie cookie =new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    System.out.println("我是next:----------"+next);
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                model.addAttribute("next",next);
                return "login";
               // return "redirect:/";
            }

        } catch (Exception e) {
            LOGGER.error("登录异常"+e.getMessage());
            return  "login";
        }
    }

    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="next",required = false)String next,
                      HttpServletResponse response){

        Map<String,String> map=userService.regsiter(username,password);
        System.out.println("我进入reg页面了,此时username为"+username+
                "password为"+password+"msf为"+map.get("msg")+"next为"+next);

        try {
            if(map.containsKey("ticket")){
                Cookie cookie =new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    System.out.println("我是next:----------"+next);
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                System.out.println("--------------------");
                model.addAttribute("msg",map.get("msg"));
                model.addAttribute("next",next);
                return "login";
            }

        } catch (Exception e) {
            LOGGER.error("注册异常"+e.getMessage());
            return  "login";
        }
    }

    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})
    public String reg(Model model,
                      @RequestParam(value="next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path={"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
