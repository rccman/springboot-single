package com.rcc.test.test.algorithm;

public class Fibonacci {
    public static int fibonacci(int n)
    {
        if(n<=0)
            return 0;
        else if(n==1)
            return 1;

        else
        {
            //当n>=2时，初始化pre=f(0)=0,post=f(1)=1,f(n)=0;
            int pre=0;
            int post=1;
            int fn=0;
            //采用循环计算斐波那契数列，通过两个临时变量pre和post保存中间结果，避免重复计算
            for(int i=2;i<=n;i++)
            {
                fn=pre+post;//fn等于其前面两个元素值的和
                //然后让pre和post分别直线他们后面的元素。
                pre=post;
                post=fn;
            }
            return fn;
        }
    }

    public static void main(String[] args) {
        //System.out.println(fibonacci(2));
        int pre = 0;
        int post = 1;
        int tmp = 0;
        for (int i = 1; i < 8; i++) {
            if (i==1){
                System.out.println(1);
            }else {
                tmp = pre + post;
                pre = post;
                post = tmp;
                System.out.println(tmp);
            }
        }
    }
}
