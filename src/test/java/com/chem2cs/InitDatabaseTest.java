package com.chem2cs;

import com.chem2cs.dao.QuestionDAO;
import com.chem2cs.dao.UserDAO;
import com.chem2cs.model.Question;
import com.chem2cs.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTest {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    UserDAO userDAO;
    @Test
    public void initDatabase(){
        System.out.println("start test");
        Random random=new Random();
        for(int i=0;i<11;i++){
            User user=new User();
            user.setId(i+1);
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("user%d",i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            user.setPassword("x3xx");
            userDAO.updatePassword(user.getPassword(),user.getId());
            Question question=new Question();
            question.setCommentCount(random.nextInt(1000));
            Date date=new Date();
            date.setTime(date.getTime()+1000*3600*i);
            question.setCreatedDate(date);
            System.out.println("当前设置时间为"+question.getCreatedDate());
            question.setUserId(i+1);
            question.setTitle(String.format("Tilte%d",i));
            question.setContent(String.format("the content%d",random.nextInt(1000)));
            questionDAO.addQuestion(question);

        }


        System.out.println(questionDAO.selectLatestQuestions(0,0,5));
    }
}
