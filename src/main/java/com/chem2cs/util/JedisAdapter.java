package com.chem2cs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d ,%s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();
/*        jedis.set("hello", "world");
        jedis.rename("hello", "new hello");
        for (String s : jedis.keys("*")) {
            System.out.println(s);
        }
        jedis.setex("hello2", 15, "world");

        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
        String listName = "list";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0, 12));
        print(4, jedis.lrange(listName, 0, 3));
        print(5, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(4, jedis.lrange(listName, 0, 12));

        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "18");
        System.out.println(jedis.hgetAll(userKey));
        jedis.hsetnx(userKey, "name", "zh");
        jedis.hsetnx(userKey, "school", "thu");
        System.out.println(jedis.hgetAll(userKey));

        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }


        System.out.println("likeKey1:" + jedis.smembers(likeKey1));
        System.out.println("likeKey2:" + jedis.smembers(likeKey2));
        System.out.println(jedis.srem(likeKey1, String.valueOf(1)));
        System.out.println(jedis.srem(likeKey1, String.valueOf(1333)));

        System.out.println(jedis.smembers(likeKey1));
        System.out.println(jedis.sunion(likeKey1, likeKey2));
        System.out.println(jedis.sdiff(likeKey1, likeKey2));
        System.out.println(jedis.sinter(likeKey1, likeKey2));
        System.out.println(jedis.scard(likeKey1));
        System.out.println(jedis.srandmember(likeKey1, 3));


        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "a");
        jedis.zadd(rankKey, 65, "ea");
        jedis.zadd(rankKey, 45, "ae");
        jedis.zadd(rankKey, 95, "arr");
        jedis.zadd(rankKey, 25, "aw");
        System.out.println(jedis.zcard(rankKey));
        System.out.println(jedis.zscore(rankKey, "a"));
        System.out.println(jedis.zrank(rankKey, "ea"));
        System.out.println(jedis.zrevrange(rankKey, 0, 3));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100")) {
            System.out.println(tuple.getElement() + ":" + tuple.getScore());
        }

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        System.out.println(jedis.zrank(setKey, "ae"));
        System.out.println(jedis.zlexcount(setKey, "[b", "(e"));
        jedis.zremrangeByLex(setKey, "[c", "+");
        System.out.println(jedis.zrange(setKey, 0, 10));

*//*        JedisPool pool=new JedisPool();
        for(int i=0;i<100;i++){
            Jedis j=pool.getResource();
            System.out.println(j.get("pv"));
        }*//*

        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("aaa");
        user.setSalt("ssss");
        user.setId(1);
        System.out.println(JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));
        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);
        System.out.println(user2.getPassword());*/

        Transaction tx=jedis.multi();
        tx.zadd("1",1,"13");
        tx.zadd("1",0,"13");
        tx.zadd("1",2,"4");
        tx.zadd("2",3,"4");
        tx.zadd("3",4,"5");
        System.out.println(tx.exec().toString());
        System.out.println(jedis.keys("*"));
        System.out.println(jedis.zrange("1",0,0));
        System.out.println(jedis.zscore("1","4")==null);




    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }


    public long saad(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        }catch (Exception e){
            logger.error("exception occurs!"+e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }catch (Exception e){
            logger.error("exception occurs!"+e.getMessage());
        }finally {
            if(tx!=null) {
                try {
                    tx.close();
                } catch (IOException e) {
                    logger.error("exception occurs!" + e.getMessage());
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key,double score, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key,start,end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> lrange(String key,long start,long end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key,start,end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


}