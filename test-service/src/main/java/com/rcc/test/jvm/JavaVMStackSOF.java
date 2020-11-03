package com.rcc.test.jvm;

/**
 * @Description: 栈 StackOverflowError
 * @Author: renchaochao
 * @Date: 2020/11/3 15:08
 **/
public class JavaVMStackSOF {

    private int length = 1;

    public void addLength(){
        length++;
        addLength();
    }

    public static void main(String[] args) {
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        try {
            javaVMStackSOF.addLength();
        } catch (Throwable e) {
            System.out.println("Stack length: "+javaVMStackSOF.length);
            throw e;
        }
    }

}
