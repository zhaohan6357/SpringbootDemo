package com.chem2cs.service;

import com.chem2cs.dao.MeassageDao;
import com.chem2cs.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MeassageDao meassageDao;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
     message.setContent(sensitiveService.filter(message.getContent()));
     return meassageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return meassageDao.getConversationDetails(conversationId,offset,limit);
    }
}
