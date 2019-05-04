package com.rcc.test.test.factory.abstractFun;

/**
 * 抽象工厂接口，生产发动机和轮胎
 */
public interface AbstractFactory {
    public Engine produceEngine();
    public Tire produceTire();
}
