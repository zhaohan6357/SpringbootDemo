package com.chem2cs.dao;


import com.chem2cs.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserDAO {
    String TABLE_NAME=" User ";
    String INSERT_FIELDS=" name,password,salt,head_url ";
    String SELECT_FIELDS=" id "+INSERT_FIELDS;
    @Insert({"insert into "+TABLE_NAME+ "( "+INSERT_FIELDS+" )" +
            " value(#{name},#{password},#{salt},#{headUrl}) "})
    int addUser(User user);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id =#{id}" })
    User selectByID(int id);

    @Update({"update ",TABLE_NAME," set password=#{0} where id= #{1}"})
    void updatePassword(String password,int id);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteByID(int id);

}
