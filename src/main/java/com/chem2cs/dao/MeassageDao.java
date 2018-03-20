package com.chem2cs.dao;

import com.chem2cs.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
@Mapper
@Service
public interface MeassageDao {
    String TABLE_NAME=" message ";
    String INSERT_FIELDS=" from_id,to_id,content,has_read,conversation_id,created_date ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "( ",INSERT_FIELDS,
            ") value (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationDetails(@Param("conversationId") String conversationId,
                                         @Param("offset") int offset,
                                         @Param("limit")int limit);

}
