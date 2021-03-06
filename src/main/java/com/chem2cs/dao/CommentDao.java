package com.chem2cs.dao;

import com.chem2cs.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface CommentDao {
    String TABLE_NAME=" comment ";
    String INSERT_FIELDS=" user_id,content,created_date,entity_id,entity_type,status ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME, "( ",INSERT_FIELDS,
            ") value (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                         @Param("entityType") int entityType);


    @Select({"select count(id) from ",TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType}"})

    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update comment set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME, " where id=#{id}"})
    Comment getCommentById(@Param("id") int id);

    @Select({"select count(id) from ",TABLE_NAME,
            " where user_id=#{userId} and entity_type=#{entityType}"})

    int getUserCommentCount(@Param("userId") int userId, @Param("entityType") int entityType);

}