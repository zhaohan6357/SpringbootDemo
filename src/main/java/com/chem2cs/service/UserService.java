package com.chem2cs.service;


import com.chem2cs.dao.UserDAO;
import com.chem2cs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getUser(int id){
        return userDAO.selectByID(id);
    }
}
