package com.chem2cs.controller;

import com.chem2cs.model.HostHolder;
import com.chem2cs.model.Message;
import com.chem2cs.model.User;
import com.chem2cs.model.ViewObject;
import com.chem2cs.service.MessageService;
import com.chem2cs.service.UserService;
import com.chem2cs.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model) {
        if(hostHolder.getUser()==null){
            return "redirect:/reglogin";
        }
        int localUserId=hostHolder.getUser().getId();
        List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations=new ArrayList<>();
        for(Message message:conversationList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targetId=message.getFromId()==localUserId?message.getToId():message.getFromId();
            vo.set("user",userService.getUser(targetId));
            vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @Param("conversationId")String conversationId) {
        try {
            List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<>();
            for(Message message:messageList){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                messageService.setReadStatus(conversationId,1);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        } catch (Exception e) {
            LOGGER.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.getUserByName(toName);
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setHasRead(0);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            LOGGER.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发信失败");
        }
    }


}
