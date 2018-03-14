package com.chem2cs.controller;

import com.chem2cs.model.Question;
import com.chem2cs.model.ViewObject;
import com.chem2cs.service.QuestionService;
import com.chem2cs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(IndexController.class);
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path={"/","/index"})
    public String index(Model model){
        List<Question> questionList=questionService.getLatestQuestions(0,0,10);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questionList){
            ViewObject viewObject=new ViewObject();
            viewObject.set("question",question);
            viewObject.set("user",userService.getUser(question.getUserId()));
            vos.add(viewObject);
        }
        model.addAttribute("vos",vos);
        return "index";
    }

}
