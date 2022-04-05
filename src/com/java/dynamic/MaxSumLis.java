package com.java.dynamic;

import java.util.Arrays;

public class MaxSumLis {

    public static void main(String[] args) {
        int[] arr = {3, 20, 4, 6, 7, 30};
            System.out.println(findMaxSumLis(arr));
    }

    public static int findMaxSumLis(int[] arr) {
        int mis[] = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            mis[i] = arr[i];
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                       mis[i] = Math.max(mis[i], arr[i]+ mis[j]);
                }
            }
        }
       int res = mis[0];
        for(int i=1; i< mis.length; i++){
            if(mis[i] > res){
                res = mis[i];
            }
        }
        return res;
    }
}
