package com.chem2cs.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chem2cs.util.JedisAdapter;
import com.chem2cs.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    public boolean fireEvent(EventModel eventModel){
        try{
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getBizEventqueueKey();
            jedisAdapter.lpush(key,json);
            return true;

        }catch (Exception e){
            return false;
        }
    }
}
