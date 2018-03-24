package com.chem2cs.controller;

import com.chem2cs.model.Question;
import com.chem2cs.model.User;
import com.chem2cs.model.ViewObject;
import com.chem2cs.service.QuestionService;
import com.chem2cs.service.UserService;
import com.chem2cs.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Controller
public class ControllerTest {
    @Autowired
    MailSender mailSender;

    @RequestMapping(path={"/mail"})
    @ResponseBody
    public String index(Model model){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "sasasa");
        for(int i=0;i<20;i++){
            mailSender.sendWithHTMLTemplate("sasa", "登陆IP异常", "mails/login_exception.html", map);
        }
        return "send mail";
    }
}
