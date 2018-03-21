package com.chem2cs.service;

import com.chem2cs.dao.MessageDao;
import com.chem2cs.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
     message.setContent(sensitiveService.filter(message.getContent()));
     return messageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetails(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }

    public   int getConversationUnreadCount( int userId, String conversationId){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }

    public void setReadStatus(String conversationId, int status){
        messageDao.setReadStatus(conversationId,status);
    }

}
