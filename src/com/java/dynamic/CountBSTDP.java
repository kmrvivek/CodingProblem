package com.java.dynamic;

import java.util.Arrays;

public class CountBSTDP {

    public static void main(String[] args) {
        getMaxCount(5);
        System.out.println(getMaxCountByFact(5));
    }

    private static int getMaxCountByFact(int n){
       /* Use catalan formula i.e catalan formula res(n) = 1/(n+1) * 2n!/(n! * n!) */
        int num = getFact(2*n);
        int denom = getFact(n);
        return  num/((n+1) * denom * denom);
    }

    private static int getFact(int n){
        if(n==0 || n==1){
            return 1;
        }
        return n * getFact(n-1);
    }

    private static void getMaxCount(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                int left = dp[j-1];
                int right = dp[i-j];
                dp[i] += left * right;
            }
        }
        System.out.println(Arrays.toString(dp));
    }
}
