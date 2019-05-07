package com.rcc.test.test.algorithm;

import java.util.Arrays;

public class MergeSort2 {
    /*public void mergeSort(int[] arr, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            int mid = (startIndex+endIndex)/2;
            mergeSort(arr,startIndex,mid);
            mergeSort(arr,mid+1,endIndex);

        }
    }
    public void merge(){

    }*/
    public static int[] merge2Arr(int[] arr1, int[] arr2) {
        int len1 = arr1.length;
        int len2 = arr2.length;
        int[] res = new int[len1 + len2];
        int i = 0, j = 0, k = 0;
        while(i < len1 && j < len2) {
            res[k++] = arr1[i] < arr2[j]? arr1[i++] : arr2[j++];
        }
        while(i < len1) {
            res[k++] = arr1[i++];
        }
        while(j < len2) {
            res[k++] = arr2[j++];
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr1= {1,4,76,5,3};
        int[] arr2={5,66,76,3,9};
        int[] aaa=merge2Arr(arr1, arr2);
        System.out.println(Arrays.toString(aaa));
    }

}
