package com.rcc.test.jvm;

/**
 * @Description: 无限new线程引发OOM
 * @Author: renchaochao
 * @Date: 2020/11/3 16:12
 **/
public class JavaVMStackOOM {

    private void dontStop(){
        while (true){

        }
    }

    private void stackLeakByThread(){
        while (true){
            new Thread(()->{
                dontStop();
            }).start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM javaVMStackOOM = new JavaVMStackOOM();
        javaVMStackOOM.stackLeakByThread();
    }
}
