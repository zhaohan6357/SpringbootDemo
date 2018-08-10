package com.chem2cs.dao;

import com.chem2cs.model.Comment;
import com.chem2cs.model.Feed;
import org.apache.ibatis.annotations.*;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface FeedDAO {
    String TABLE_NAME=" feed ";
    String INSERT_FIELDS=" data,type,created_date,user_id ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME, "( ",INSERT_FIELDS,
            ") value (#{data},#{type},#{createdDate},#{userId})"})
    int addFeed(Feed feed);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME, " where id=#{id}"})
    Feed getFeedById(int id);

    List<Feed> selectUserFeeds(@Param("maxId")int maxId, @Param("userIds")List<Integer> userIds,
                               @Param("count") int count);


}