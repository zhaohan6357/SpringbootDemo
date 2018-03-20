package com.chem2cs.service;

import com.chem2cs.dao.QuestionDAO;
import com.chem2cs.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;
    public int addQuestion(Question question){
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //filter
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDAO.addQuestion(question)>0?question.getId():0;
    }
    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

    public Question getQuestion(int id){
        return questionDAO.selectByID(id);
    }

    public int updateCommentCount(int count,int id){
        return questionDAO.updateCommentCount(count,id);
    }
}
