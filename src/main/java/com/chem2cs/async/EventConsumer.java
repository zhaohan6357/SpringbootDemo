package com.chem2cs.async;


import com.alibaba.fastjson.JSON;
import com.chem2cs.util.JedisAdapter;
import com.chem2cs.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {

    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    JedisAdapter  jedisAdapter;
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null&&beans.size()!=0){
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
               // System.out.println("---hanlder:"+entry.getKey()+"  eventHandeler"+entry.getValue().getSupportEventTypes());
                List<EventType> eventTypes=entry.getValue().getSupportEventTypes();
                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }else{
            logger.error("no handler!!");
        }
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key= RedisKeyUtil.getBizEventqueueKey();
                   List<String> events=jedisAdapter.brpop(0,key);
                   for(String message:events){
                       if(message.equals(key)){
                           continue;
                       }
                       EventModel eventModel= JSON.parseObject(message,EventModel.class);
                       if(!config.containsKey(eventModel.getType())){
                           logger.error("不能识别的事件");
                           continue;
                       }
                       for(EventHandler handler:config.get(eventModel.getType())){
                           handler.doHandle(eventModel);
                       }
                   }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}