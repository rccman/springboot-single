package com.rcc.test.test.factory.abstractFun;

/**
 * 货车发动机实现类
 */
public class TruckEngine implements Engine{
    @Override
    public void running() {
        System.out.println("Running with truck");
    }
}
