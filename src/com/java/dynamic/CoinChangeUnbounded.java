package com.java.dynamic;

public class CoinChangeUnbounded {

    public static void main(String[] args) {
        int[] arr = {1, 2};
        int sum = 4;
        System.out.println(coinWays(arr, arr.length, sum));
        System.out.println(coinWaysDP(arr, arr.length, sum));
    }

    private static int coinWaysDP(int[] coins, int n, int sum){
        int[][] dp = new int[n+1][sum+1];
        for(int i=1; i<=sum; i++){
            dp[0][i] = 0;
        }
        for(int i=0; i<=n; i++){
            dp[i][0] = 1;
        }
        for(int i=1; i<=n; i++){
            for(int j=1; j<=sum; j++){
                if(coins[i-1] <= j){
                    dp[i][j] = dp[i][j-coins[i-1]] + dp[i-1][j];
                } else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[n][sum];
    }

    private static int coinWays(int[] coins, int n, int sum){
        if(sum == 0){
            return 1;
        }
        if(n <= 0){
            return 0;
        }
        int ways = 0;
        if(coins[n-1] <= sum){
            ways += coinWays(coins, n, sum-coins[n-1]) + coinWays(coins, n-1, sum);
        } else {
            ways += coinWays(coins, n-1, sum);
        }
        return ways;
    }
}
