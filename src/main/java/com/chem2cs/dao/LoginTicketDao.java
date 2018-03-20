package com.chem2cs.dao;

import ch.qos.logback.classic.db.names.TableName;
import com.chem2cs.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Service
public interface LoginTicketDao {
    String TABLE_NAME=" login_ticket ";
    String INSERT_FIELDS=" user_id,expired,status,ticket ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME,"( ",INSERT_FIELDS," )",
            "value(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("status") int status,@Param("ticket") String ticket);
}
