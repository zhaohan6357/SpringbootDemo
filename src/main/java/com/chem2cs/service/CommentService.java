package com.chem2cs.service;

import com.chem2cs.dao.CommentDao;
import com.chem2cs.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    SensitiveService sensitiveService;
    public List<Comment> getCommentByEntity(int entityId, int entityType){

        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //filter
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment)>0?comment.getId():0;
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public int deleteCommentById(int id){
        return commentDao.updateStatus(id,0);
    }

    public Comment getCommentById(int id){
        return commentDao.getCommentById(id);
    }

}
