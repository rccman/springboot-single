package com.rcc.test.test.algorithm;

import java.util.Arrays;

/**
 *
 */
public class MergeSortDuplicate {
    public static void main(String[] args) {
        int[] arr = {54,34,1,7,8,3,5,9,1,8,0,3};
        int[] tmp = new int[arr.length];
        mergeSort(arr,0,arr.length-1,tmp);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void mergeSort(int[] arr, int startIndex, int endIndex, int[] tmp) {
        if (startIndex < endIndex) {
            int mid = (startIndex+endIndex)/2;
            mergeSort(arr,startIndex,mid,tmp);
            mergeSort(arr,mid+1,endIndex,tmp);
            merge(arr,startIndex,mid,endIndex,tmp);
        }
    }

    private static void merge(int[] arr, int startIndex, int mid, int endIndex, int[] tmp) {
        int i = 0;
        int j = startIndex;
        int k = mid+1;
        //控制重复的个数
        int n = 0;
        while (j<=mid && k<=endIndex){
            if(arr[j] < arr[k]){
                tmp[i++] = arr[j++];
            }else if(arr[j] == arr[k]){
                tmp[i++] = arr[j++];
                //末尾值存重复元素
                tmp[endIndex-n++] = arr[k++];
            }else{
                tmp[i++] = arr[k++];
            }
        }
        while (j <= mid){
            tmp[i++] = arr[j++];
        }
        while (k <= endIndex){
            tmp[i++] = arr[k++];
        }
        for (int t = 0; t < i; t++) {
            arr[startIndex + t] = tmp[t];
        }
        while (startIndex + i <=  endIndex){
            arr[startIndex + i] = tmp[i];
            i++;
        }
    }


}
