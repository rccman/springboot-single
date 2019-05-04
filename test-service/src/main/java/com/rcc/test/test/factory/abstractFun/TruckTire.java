package com.rcc.test.test.factory.abstractFun;

/**
 * 货车轮胎实现类
 */
public class TruckTire implements Tire {
    @Override
    public void scroll() {
        System.out.println("Scroll with truck");
    }
}
