package com.chem2cs.controller;

import com.chem2cs.model.Question;
import com.chem2cs.model.User;
import com.chem2cs.model.ViewObject;
import com.chem2cs.service.QuestionService;
import com.chem2cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


//@Controller
public class ControllerTest {
    @Autowired
    UserService userService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(Model model){
        User user=userService.getUser(2);
        return user.toString();
    }
}
