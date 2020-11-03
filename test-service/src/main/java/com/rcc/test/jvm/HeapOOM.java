package com.rcc.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 堆内存溢出
 * @Author: renchaochao
 * @Date: 2020/10/30 14:55
 **/
public class HeapOOM {

    static class OOMObject{

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();

        while (true){
            list.add(new OOMObject());
        }
    }
}
