package com.rcc.test.test.factory.abstractFun;

/**
 * 客车发动机实现类
 */
public class BusEngine implements Engine{
    @Override
    public void running() {
        System.out.println("Running with bus");
    }
}
