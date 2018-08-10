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

    @RequestMapping(path={"/test"})
    public String test(Model model){
        List<test> feeds=new ArrayList<>();
        for(int i=0;i<10;i++){
            feeds.add(new test(i%2==0?1:4));
        }
        model.addAttribute("feeds",feeds);
        return "test2";

    }
}
