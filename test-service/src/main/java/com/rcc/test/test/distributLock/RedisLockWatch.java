package com.rcc.test.test.distributLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

public class RedisLockWatch extends  Thread {
    private String auctionCode;
    private static String host = "172.16.13.61";
    private static int port = 6379;
//    private static Jedis jedis = new Jedis(host,port);


    public RedisLockWatch(String auctionCode) {
        super(auctionCode);
        this.auctionCode = auctionCode;
    }
    private static int bidPrice = 100;

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "主线程运行开始!");
        //更改key为a的值
        Jedis jedis=new Jedis(host,port);
        jedis.set("goodsprice","0");
        System.out.println("输出初始化值："+jedis.get("goodsprice"));
        jedis.close();
        RedisLockWatch thread1 = new RedisLockWatch("A001");
        RedisLockWatch thread2  = new RedisLockWatch("B001");
        thread1.start();
        thread2.start();
        try{
            thread1.join();
            thread2.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "主线程运行结束!");
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程运行开始 ");
        Jedis jedis=new Jedis(host,port);
        try {
            if(Thread.currentThread().getName()=="B001"){
                sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //监视KEY
        jedis.watch("goodsprice");
        //A先进
        String v =  jedis.get("goodsprice");
        Integer iv = Integer.valueOf(v);
        //条件都给过
        if(bidPrice > iv){
            Transaction tx = jedis.multi();// 开启事务
            Integer bp = iv + 100;
            //出价成功，事务未提交
            tx.set("goodsprice",String.valueOf(bp));
            System.out.println("子线程" + auctionCode + "set成功");
            try {
                if(Thread.currentThread().getName()=="A001"){
                    sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Object> list = tx.exec();
            if (list == null ||list.size()==0) {
                System.out.println("子线程" + auctionCode + ",出价失败");
            }else{
                System.out.println("子线程"+this.auctionCode +", 出价："+ jedis.get("goodsprice") +"，出价时间："
                        + System.nanoTime());
            }
        }else{
            System.out.println("出价低于现有价格！");
        }
        jedis.close();
        System.out.println(Thread.currentThread().getName() + "线程运行结束");
    }

}
