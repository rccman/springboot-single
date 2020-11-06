package com.rcc.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 运行时常量池OOM
 * @Author: renchaochao
 * @Date: 2020/11/3 16:36
 **/
public class RunTimeConstantPollOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        short i = 0;
        while (true){
            list.add(String.valueOf(i++).intern());
        }
    }
}
