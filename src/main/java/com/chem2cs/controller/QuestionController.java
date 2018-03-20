package com.chem2cs.controller;

import com.chem2cs.model.*;
import com.chem2cs.service.CommentService;
import com.chem2cs.service.QuestionService;
import com.chem2cs.service.UserService;
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
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
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

    @RequestMapping(path={"/question/{qid}"})
    public String questionDetail(@PathVariable(value = "qid") int qid,
                                 Model model) {
        Question question = questionService.getQuestion(qid);
        model.addAttribute("question", question);
        User user=null;
        if(hostHolder.getUser()!=null){
            user=hostHolder.getUser();
        }
        model.addAttribute("user",user);
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
    }

}
