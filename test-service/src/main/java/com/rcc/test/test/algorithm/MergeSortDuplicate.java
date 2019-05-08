package com.rcc.test.test.algorithm;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 归并排序加去重
 */
public class MergeSortDuplicate {
    public static void main(String[] args) {
        //原数组
        int[] arr = {54,34,1,7,8,3,5,9,1,8,0,3};
        //临时数组
        int[] tmp = new int[arr.length];
        //重复数据的个数
        int n = 0;
        //归并排序去重核心算法
        n = mergeSort(arr,0,arr.length-1,tmp,n);
        //截取排序去重后的数组
        arr = ArrayUtils.subarray(arr,0,arr.length-n);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /**
     * 递归，先拆分，再排序
     * @param arr
     * @param startIndex
     * @param endIndex
     * @param tmp
     * @param n
     * @return
     */
    public static int mergeSort(int[] arr, int startIndex, int endIndex, int[] tmp, int n) {
        if (startIndex < endIndex) {
            int mid = (startIndex+endIndex)/2;
            n = mergeSort(arr,startIndex,mid,tmp, n);
            n = mergeSort(arr,mid+1,endIndex,tmp, n);
            n = merge(arr,startIndex,mid,endIndex,tmp,n);
        }
        return n;
    }

    /**
     * 排序，重复数据移到数组尾部
     * @param arr
     * @param startIndex
     * @param mid
     * @param endIndex
     * @param tmp
     * @param n
     * @return
     */
    private static int merge(int[] arr, int startIndex, int mid, int endIndex, int[] tmp, int n) {
        int i = 0;
        int j = startIndex;
        int k = mid+1;
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
        return n;
    }


}
