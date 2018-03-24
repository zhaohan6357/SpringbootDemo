package com.chem2cs.controller;

import com.chem2cs.model.*;
import com.chem2cs.service.CommentService;
import com.chem2cs.service.FollowService;
import com.chem2cs.service.QuestionService;
import com.chem2cs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
   private static  final Logger LOGGER=  LoggerFactory.getLogger(HomeController.class);
   @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @RequestMapping(path={"/user/{userId}"})

    public String indexUser(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestions(userId,0,10));
        User user=userService.getUser(userId);
        ViewObject vo=new ViewObject();
        vo.set("user",user);
        vo.set("followeeCount",followService.getFolloweeCount(EntityType.ENTITY_USER,userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("commentCount", commentService.getUserCommentCount(userId,EntityType.ENTITY_QUESTION));
        if(hostHolder.getUser()!=null){
            vo.set("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId));
        }else{
            vo.set("followed",false);
        }
        model.addAttribute("profileUser", vo);

        return "profile";
    }

    @RequestMapping(path={"/","/index"})
    public String index(Model model){
       //  System.out.println(">>>>>>user:"+hostHolder.getUser());
       //  model.addAttribute("user",hostHolder.getUser());
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }
    private List<ViewObject> getQuestions(int userID,int offset,int limit){
        List<Question> questionList=questionService.getLatestQuestions(userID,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question:questionList){
            ViewObject viewObject=new ViewObject();
            viewObject.set("question",question);
            viewObject.set("user",userService.getUser(question.getUserId()));
            vos.add(viewObject);
        }
        return vos;
    }

}
