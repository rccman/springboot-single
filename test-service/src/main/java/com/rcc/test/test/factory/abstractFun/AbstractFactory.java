package com.rcc.test.test.factory.abstractFun;

/**
 * 抽象工厂接口，生产发动机和轮胎
 */
public interface AbstractFactory {
    //生产发动机
    public Engine produceEngine();
    //生产轮胎
    public Tire produceTire();
}
