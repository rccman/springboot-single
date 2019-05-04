package com.rcc.test.test.factory.abstractFun;

/**
 * 货车工厂
 */
public class TruckFactory implements AbstractFactory {
    @Override
    public Engine produceEngine() {
        return new TruckEngine();
    }

    @Override
    public Tire produceTire() {
        return new TruckTire();
    }
}
