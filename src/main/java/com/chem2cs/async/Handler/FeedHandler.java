package com.chem2cs.async.Handler;

import com.alibaba.fastjson.JSONObject;
import com.chem2cs.async.EventHandler;
import com.chem2cs.async.EventModel;
import com.chem2cs.async.EventType;
import com.chem2cs.model.*;
import com.chem2cs.service.*;
import com.chem2cs.util.JedisAdapter;
import com.chem2cs.util.RedisKeyUtil;
import com.chem2cs.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;
    private  String buildFeedData(EventModel model){
        Map<String,String> map=new HashMap<>();
        User actor=userService.getUser(model.getActorId());
        if(actor==null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());
        if(model.getType()==EventType.COMMENT||
                ( model.getType()==EventType.FOLLOW&&model.getEntityType()==EntityType.ENTITY_QUESTION)){
            Question question=questionService.getQuestion(model.getEntityId());
            if(question==null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
    @Override
    public void doHandle(EventModel model) {
   /*     Random r=new Random();
        model.setActorId(1+r.nextInt(10));*/

        Feed feed=new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if(feed.getData()==null){
            return;
        }
        feedService.addFeed(feed);

        //push for all the followers of the event;
        List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        followers.add(0);
        for(int follower:followers){
            String timelineKey= RedisKeyUtil.getTimeLineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
