package com.chem2cs.dao;

import com.chem2cs.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id,to_id,content,has_read,conversation_id,created_date ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            ") value (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationDetails(@Param("conversationId") String conversationId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);


    @Select({"select ", INSERT_FIELDS, ", count(id) as id from ( select * from", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by created_date desc) " +
                    "tt group by conversation_id " +
                    "order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    @Update({"update message set has_read=#{status} where conversation_id=#{conversationId}"})
    void setReadStatus(@Param("conversationId") String conversationId,
                       @Param("status") int status);
}
