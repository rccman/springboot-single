package com.rcc.test.test.algorithm;

public class ArrayMax {
    public static void main(String[] args) {
        int[] arr = {-1,2,6,-9,9,7};
        int maxSum = getMaxSum(arr);
        System.out.println(maxSum);
    }

    private static int getMaxSum(int[] arr) {
        if(arr==null){
            return 0;
        }
        int sum = 0;
        for(int i=0;i<arr.length;i++){
            int tmpMax = 0;
            for (int j=i;j<arr.length;j++){
                tmpMax += arr[j];
                if(tmpMax > sum){
                    sum = tmpMax;
                }
            }
        }
        return sum;
    }
}
