package com.chem2cs.controller;

import com.chem2cs.model.*;
import com.chem2cs.service.*;
import com.chem2cs.util.JedisAdapter;
import com.chem2cs.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping (path={"/pullfeeds"})
    public String getPullFeeds(Model model){
        int localUserId=hostHolder.getUser()==null?0:hostHolder.getUser().getId();
        List<Integer> followees=new ArrayList<>();
        if(localUserId!=0){
            followees=followService.getFollowees(EntityType.ENTITY_USER,localUserId,Integer.MAX_VALUE);
        }
        List<Feed> feeds=feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    @RequestMapping (path={"/pushfeeds"})
    public String getPushFeeds(Model model){
        int localUserId=hostHolder.getUser()==null?0:hostHolder.getUser().getId();
        List<String> feedIds=jedisAdapter.lrange(RedisKeyUtil.getTimeLineKey(localUserId),0,10);
        List<Feed> feeds=new ArrayList<>();
        for(String feedId:feedIds){
           Feed feed= feedService.getById(Integer.parseInt(feedId));
           if(feed==null){
               continue;
           }
           feeds.add(feed);
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
