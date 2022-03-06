package com.xm;

import redis.clients.jedis.Jedis;

/**
 * @author mintFM
 * @create 2021-10-25 15:16
 */
public class SecKill_redis {

    public static void main(String[] args) {
        //测试redis是否连接成功
        Jedis jedis = new Jedis("192.168.153.129",6379);
        System.out.println(jedis.ping());
        jedis.close();
    }

    //编写实现秒杀的过程
    public static boolean doSecKill(String uid,String prodid) {
        //1.对uid用户ID和Proid 商品进行是否非空的判断
        if (uid == null || prodid == null) {
            //如果为空，根本没法进行秒杀系统
            return false;
        }

        //2.连接Redis，进行后续操作
        Jedis jedis = new Jedis("192.168.153.129",6379);

        //3.拼接Key值，通过key来获取相应的value值
        //设置库存的key值
        String kcKey = "sk:" + prodid + ":qt";
        //设置秒杀成功用户的key值
        String userKey = "sk:" + prodid + ":user";

        //4.获取库存值，如果库存是null，则表示秒杀还没开始
        String kc = jedis.get(kcKey);
        if (kc == null) {
            //库存为空，秒杀为开始，退出系统
            System.out.println("秒杀还未开始，请耐心等待！");
            jedis.close();
            return false;
        }

        //5.判断用户是否进行了重复秒杀的操作
        if (jedis.sismember(userKey,uid)) {
            //如果用户存在，表示秒杀成功了，则不能再继续秒杀了，所以退出秒杀系统
            System.out.println("已经秒杀成功，不能重复秒杀！");
            jedis.close();
            return false;
        }

        //6.对商品的数量进行判断，如果商品的数量小于1，则没有商品可以秒杀了，所以秒杀结束
        if (Integer.parseInt(kc) <= 0) {
            System.out.println("秒杀已经结束！");
            jedis.close();
            return false;
        }


        //7.秒杀过程的实现
        //库存减一
        jedis.decr(kcKey);
        //将秒杀成功的用户添加到用户清单中
        jedis.sadd(userKey,uid);
        System.out.println("秒杀成功了！");
        jedis.close();
        return true;

    }
}
