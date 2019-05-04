package com.rcc.test.test.factory.abstractFun;

/**
 * 客车轮胎实现类
 */
public class BusTire implements Tire {
    @Override
    public void scroll() {
        System.out.println("Scroll with bus");
    }
}
