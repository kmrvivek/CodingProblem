package com.java.dynamic;

import java.util.Arrays;

public class MinJump {

    // Take jump from last point
    public static void main(String[] args) {
        int[] arr = {3, 4, 2, 1, 2, 1};
        System.out.println(getMinJump(arr, arr.length));
        System.out.println(getMinJumpDP(arr, arr.length));
    }

    private static int getMinJumpDP(int[] arr, int n){
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i=1; i<n; i++){
           for (int j=0; j<i; j++){
               if(arr[j]+j >= i){
                   if(dp[j] != Integer.MAX_VALUE){
                       dp[i] = Math.min(dp[i], dp[j]+1);
                   }
               }
           }
        }
        return dp[n-1];
    }

    private static int getMinJump(int[] arr, int n){
        if(n == 1){
            return 0;
        }
        int res = Integer.MAX_VALUE;
        for(int i=0; i<n-1; i++){
            if(i+arr[i] >= n-1){
                int sub_res = getMinJump(arr, i+1);
                if(sub_res != Integer.MAX_VALUE){
                    res = Math.min(res, sub_res+1);
                }
            }
        }
        return res;
    }
}
