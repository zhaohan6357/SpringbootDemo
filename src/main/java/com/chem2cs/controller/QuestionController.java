package com.chem2cs.controller;

import com.chem2cs.model.*;
import com.chem2cs.service.*;
import com.chem2cs.util.WendaUtil;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    LikeService likeService;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @RequestMapping(value="/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam(value = "title")String title,
                              @RequestParam(value ="content")String content){
        try{
            Question question=new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(999);
                // question.setUserId(WendaUtil.ANONYMOUL_USERID);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0);
            }

        }catch (Exception e){
            LOGGER.error("增加题目失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"添加失败");
    }

/*    @RequestMapping(path={"/question/{qid}"})
    public String questionDetail(@PathVariable(value = "qid") int qid,
                                 Model model) {
        Question question = questionService.getQuestion(qid);
        model.addAttribute("question", question);
 *//*       User user=null;
        if(hostHolder.getUser()!=null){
            user=hostHolder.getUser();
        }
        model.addAttribute("user",user);*//*
        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }*/

    @RequestMapping(path={"/question/{qid}"})
    public String questionDetail(@PathVariable(value = "qid") int qid,
                                 Model model) {
        Question question = questionService.getQuestion(qid);
        model.addAttribute("question", question);
 /*       User user=null;
        if(hostHolder.getUser()!=null){
            user=hostHolder.getUser();
        }
        model.addAttribute("user",user);*/
        List<ViewObject> followUsers=new ArrayList<>();
        List<Integer> users= followService.getFollowers(EntityType.ENTITY_QUESTION,qid,0,10);
        for(Integer uid:users){
            ViewObject vo=new ViewObject();
            User user=userService.getUser(uid);
            if(user==null){
                continue;
            }
            vo.set("name",user.getName());
            vo.set("headUrl",user.getHeadUrl());
            vo.set("id",uid);
            followUsers.add(vo);
        }

        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }


        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
           if(hostHolder.getUser()==null){
               vo.set("liked",0);
           }else{
               vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
           }
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }

}
