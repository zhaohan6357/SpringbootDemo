package com.chem2cs.async.Handler;

import com.chem2cs.async.EventHandler;
import com.chem2cs.async.EventModel;
import com.chem2cs.async.EventType;
import com.chem2cs.model.Message;
import com.chem2cs.model.User;
import com.chem2cs.service.MessageService;
import com.chem2cs.service.UserService;
import com.chem2cs.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user=userService.getUser(model.getActorId());
        if(model.getType().equals(EventType.LOGIN)) {
            message.setContent("user " + user.getName() + " login sucess give a like for your comment, http:" +
                    "//127.0.0.1:8080/question/" + model.getExt("questionId"));
        }else{
            message.setContent("user " + user.getName() + " give a like for your comment, http:" +
                    "//127.0.0.1:8080/question/" + model.getExt("questionId"));
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE,EventType.LOGIN);
    }
}
