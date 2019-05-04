package com.rcc.test.test.single;

/**
 * 单例模式
 */
public class Signleton {
    private volatile static Signleton uniqueInstance;

    private Signleton(){

    }

    public static Signleton getUniqueInstance() {
        if(uniqueInstance == null){
            synchronized(Signleton.class){
                if(uniqueInstance == null){
                    return new Signleton();
                }
            }
        }
        return uniqueInstance;
    }
}
