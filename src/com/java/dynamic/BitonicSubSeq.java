package com.java.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BitonicSubSeq {

     //Bitonic seq is first increasing order than decreasing
    // int arr[] = {1, 11, 2, 10, 4, 5, 2, 1}
    // Ans = 1, 2, 10, 5, 2, 1
    // int arr[] = {30, 20 , 10}; --> 3
    // ans = 30, 20 ,10
    // int arr[] = {12, 11, 40, 5 , 3 ,1} -> 5
    // ans = 12/11, 40, 5, 3, 1

    public static void main(String[] args) {
        int arr[] = {1, 11, 2, 10, 4, 5, 2, 1};
        /* Find Longest Increasing Sequence and reverse Decreasing Sequence
            then take pivot max sum - 1 will be answer;
         */
        int longSol = findMaxBitonicSeq(arr);
        System.out.println(longSol);

    }


    private static int findCeil(List<Integer> nums, int val, int l, int r){
        while(l<r){
            int mid = l + (r -l)/2;
            if(nums.get(mid) >= val){
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return r;
    }

    private static int findMaxBitonicSeq(int[] arr) {
        int lis[] = new int[arr.length];
        int dis[] = new int[arr.length];

        for(int i=0; i< arr.length; i++){
            lis[i] = 1;
            for (int j=0; j<i; j++){
                if(arr[i] > arr[j]){
                    lis[i] = Math.max(lis[i], 1+lis[j]);
                }
            }
        }

        for(int i= arr.length-1; i >=0; i--){
            dis[i] = 1;
            for (int j=arr.length-1; j>i; j--){
                if(arr[i] > arr[j]){
                    dis[i] = Math.max(dis[i], 1 + dis[j]);
                }
            }
        }
        System.out.println(Arrays.toString(lis));
        System.out.println(Arrays.toString(dis));
        int sum = lis[0]+ dis[0];
        for(int i =1; i<arr.length; i++){
            if(sum < lis[i] + dis[i]){
                sum = lis[i]+dis[i];
            }
        }
        return  sum-1;
    }

}
