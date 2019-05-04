package com.rcc.test.test.factory.abstractFun;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        //客车
        AbstractFactory busFactory = new BusFactory();
        Engine busEngine = busFactory.produceEngine();
        busEngine.running();
        Tire busTire = busFactory.produceTire();
        busTire.scroll();
        //货车
        AbstractFactory truckFactory = new TruckFactory();
        Engine truckEngine = truckFactory.produceEngine();
        truckEngine.running();
        Tire truckTire = truckFactory.produceTire();
        truckTire.scroll();

    }
}
