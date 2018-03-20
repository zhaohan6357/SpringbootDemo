package com.chem2cs.dao;


import com.chem2cs.model.Question;
import com.chem2cs.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface QuestionDAO {
    String TABLE_NAME=" Question ";
    String INSERT_FIELDS=" title,content,user_id,created_date,comment_count ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;
    @Insert({"insert into "+TABLE_NAME+ "( "+INSERT_FIELDS+" )" +
            " value(#{title},#{content},#{userId},#{createdDate},#{commentCount}) "})
    int addQuestion(Question question);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id =#{id}" })
    Question selectByID(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userid,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteByID(int id);

    @Update({"update question set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("commentCount") int commentCount,@Param("id") int id);

}
