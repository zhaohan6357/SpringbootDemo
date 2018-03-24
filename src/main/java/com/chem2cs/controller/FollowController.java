package com.chem2cs.controller;

import com.chem2cs.async.EventHandler;
import com.chem2cs.async.EventModel;
import com.chem2cs.async.EventProducer;
import com.chem2cs.async.EventType;
import com.chem2cs.model.*;
import com.chem2cs.service.*;
import com.chem2cs.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class FollowController {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(FollowController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @RequestMapping(value="/followUser",method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam(value = "userId")int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        boolean ret=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret? 0:1,String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER,hostHolder.getUser().getId())));
    }

    @RequestMapping(value="/unfollowUser",method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam(value = "userId")int userId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId).setActorId(hostHolder.getUser().getId()));
        return WendaUtil.getJSONString(ret? 0:1,String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER,hostHolder.getUser().getId())));
    }


    @RequestMapping(value="/followQuestion",method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam(value = "questionId")int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        Question q=questionService.getQuestion(questionId);
        if(q==null){
            return WendaUtil.getJSONString(1,"the question is not existed!");
        }

        boolean ret=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(questionId).setActorId(hostHolder.getUser().getId()));

        Map<String,Object> info=new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
        return WendaUtil.getJSONString(ret? 0:1,info);
    }

    @RequestMapping(value="/unfollowQuestion",method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion (@RequestParam(value = "questionId")int questionId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        Question q=questionService.getQuestion(questionId);
        if(q==null){
            return WendaUtil.getJSONString(1,"the question is not existed!");
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(),EntityType.ENTITY_USER,questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(questionId).setActorId(hostHolder.getUser().getId()));

        Map<String,Object> info=new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
        return WendaUtil.getJSONString(ret? 0:1,info);
    }

    @RequestMapping(value="/user/{uid}/followees",method = {RequestMethod.GET})
    public String followees(Model model,@PathVariable(value = "uid")int userId){
        List<Integer> followeeIds=followService.getFollowees(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followees",getUsersInfo(hostHolder.getUser().getId(),followeeIds));
        }else{
            model.addAttribute("followees",getUsersInfo(0,followeeIds));
        }
        return "followees";

    }
    private List<ViewObject> getUsersInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> userInfos=new ArrayList<>();
        for(Integer uid:userIds){
            User user=userService.getUser(uid);
            if(user==null){
                continue;
            }
            ViewObject vo=new ViewObject();
            vo.set("usre",user);
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            vo.set("followeeCount",followService.getFolloweeCount(EntityType.ENTITY_USER,uid));
           if(localUserId!=0){
               vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
           }else{
               vo.set("followed",false);
           }
           userInfos.add(vo);
        }
        return userInfos;
    }

    @RequestMapping(value="/user/{uid}/followers",method = {RequestMethod.GET})
    public String followers (Model model,@PathVariable(value = "uid")int userId){
        List<Integer> followerIds=followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followerIds));
        }else{
            model.addAttribute("followers",getUsersInfo(0,followerIds));
        }
        return "followers";

    }



}
