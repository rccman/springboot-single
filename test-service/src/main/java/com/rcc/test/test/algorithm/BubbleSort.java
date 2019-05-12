package com.rcc.test.test.algorithm;

public class BubbleSort {
    public static void main(String[] args) {
        int[] arr={4,2,7,-1,-8,9,3};
//        bubbleSort(arr);
        bubbleSortDesc(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    /**
     * 升序
     * @param arr
     */
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = 0; j < arr.length-1-i; j++) {
                if(arr[j]>arr[j+1]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
    }

    /**
     * 降序
     * @param arr
     */
    private static void bubbleSortDesc(int[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = 0; j < arr.length-1-i; j++) {
                if(arr[j] < arr[j+1]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
    }
}
