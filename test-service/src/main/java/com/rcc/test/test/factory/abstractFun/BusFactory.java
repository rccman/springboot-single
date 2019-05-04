package com.rcc.test.test.factory.abstractFun;

/**
 * 客车工厂
 */
public class BusFactory implements AbstractFactory {
    @Override
    public Engine produceEngine() {
        return new BusEngine();
    }

    @Override
    public Tire produceTire() {
        return new BusTire();
    }
}
