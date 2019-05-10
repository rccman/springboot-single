package com.rcc.test.test.arrShift;

public class ArryShift {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5};
        int k = 1;
        arrShift(arr,8);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    private static void arrShift(int[] arr, int k) {
        k%=arr.length;
        while (k>0){
            int tmp = arr[arr.length-1];
            for (int i = arr.length-1; i > 0; i--) {
                arr[i] = arr[i-1];
            }
            arr[0] = tmp;
            k--;
        }
    }
}
