package com.chem2cs.controller;

import com.chem2cs.model.Comment;
import com.chem2cs.model.EntityType;
import com.chem2cs.model.HostHolder;
import com.chem2cs.service.CommentService;
import com.chem2cs.service.QuestionService;
import com.chem2cs.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(CommentController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(value = "/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {
            Comment comment=new Comment();
            comment.setContent(content);
            if(hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                comment.setUserId(WendaUtil.ANONYMOUL_USERID);
                //return "redirect:/reglogin";
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());

            questionService.updateCommentCount(count,comment.getEntityId());

        } catch (Exception e) {
            LOGGER.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/question/"+questionId;

    }

}
