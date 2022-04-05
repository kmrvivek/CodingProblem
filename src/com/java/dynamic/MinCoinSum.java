package com.java.dynamic;

import java.util.Arrays;

public class MinCoinSum {

    public static void main(String[] args) {
        int[] coins = {3, 4, 1};
        int val = 5;
        System.out.println(getMin(coins, coins.length, val));
        System.out.println(getMinCoinDp(coins, coins.length, val));
    }

    private static int getMin(int[] coins, int n, int val){
        if(val == 0){
            return 0;
        }
        int res = Integer.MAX_VALUE;
        for(int i=0; i<n; i++){
            if(val>= coins[i]){
                int sub_res = getMin(coins, n, val-coins[i]);
                if(sub_res != Integer.MAX_VALUE){
                    res = Math.min(res, sub_res+1);
                }
            }
        }
        return res;
    }

    private static int getMinCoinDp(int[] coins, int n, int value){
        int[] res = new int[value+1];
        Arrays.fill(res, Integer.MAX_VALUE);
        res[0] = 0;
        for(int i=1; i<=value; i++){
            for(int j=0; j<n; j++){
                if(coins[j] <= i){
                    res[i] = Math.min(res[i], res[i-coins[j]]+1);
                }
            }
        }
        return res[value];
    }
}
