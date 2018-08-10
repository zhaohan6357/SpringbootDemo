package com.chem2cs.util;

import com.chem2cs.model.EntityType;
import org.apache.commons.lang3.builder.StandardToStringStyle;

public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENTQUEUE="EVENTQUEUE";

    private static String BIZ_FOLLOWER="FOLLOWER";
    private static String BIZ_FOLLOWEE="FOLLOWEE";
    private static String BIZ_TIMELINE="TIMELINE";

    public static  String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }//the key of set of users that followed the entity


    public static  String getFolloweeKey(int entityType,int userId){
        return BIZ_FOLLOWEE+SPLIT+String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    } //the key of set of entities that user with userId followed

    public static String getTimeLineKey(int userId){
        return BIZ_TIMELINE+SPLIT+String.valueOf(userId);
    }

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public  static String getBizEventqueueKey(){
        return BIZ_EVENTQUEUE;
    }

}
