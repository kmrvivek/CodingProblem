package com.java.dynamic;

public class MaxCuts {

    public static void main(String[] args) {
        System.out.println(recMaxCuts(5, 1, 2, 3));
        System.out.println(dpMaxCuts(5, 1, 2, 3));
        System.out.println(dpMaxCuts(5, 2, 2, 2));
    }

    private static int dpMaxCuts(int n, int a , int b, int c){
        int[] dp = new int[n+1];
        for (int i=1; i<=n; i++){
            dp[i] = -1;
            if(i-a >= 0) dp[i] = Math.max(dp[i], dp[i-a]);
            if(i-b >= 0) dp[i] = Math.max(dp[i], dp[i-b]);
            if(i-c >= 0) dp[i] = Math.max(dp[i], dp[i-c]);
            if(dp[i] != -1){
                dp[i]++;
            }
        }
        return dp[n];
    }

    private static int recMaxCuts(int n, int a, int b, int c){
        if(n < 0){
            return -1;
        }
        if(n == 0){
            return 0;
        }
        int res = Math.max(Math.max(recMaxCuts(n-a, a, b, c), recMaxCuts(n-b, a, b, c)),
                recMaxCuts(n-c, a, b, c));
        if(res == -1){
            return res;
        }
        return res+1;
    }
}
