package com.java.dynamic;

public class MinPagesRead {

    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40};
        int k = 2;
        System.out.println(minPages(arr, arr.length, k));
        System.out.println(getMinPagesDP(arr, arr.length, k));
    }

    private static int getMinPagesDP(int[] arr, int n, int k){
        int[][] dp = new int[k+1][n+1];
        for(int i=1; i<k; i++){
            dp[i][1] = arr[0];
        }
        for(int i=1; i<n; i++){
            dp[1][i] = sum(arr, 0, i);
        }
        for(int i=2; i<=k; i++){
            for(int j=2; j<=n; j++){
                dp[i][j] = Integer.MAX_VALUE;
                for(int p=1; p<j; p++){
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[i-1][p], sum(arr, p, j)));
                }
            }
        }
        return dp[k][n];
    }

    private static int minPages(int[] arr, int n, int k){
        if(k == 1){
            return sum(arr, 0, n);
        }
        if(n == 1){
            return arr[0];
        }
        int res = Integer.MAX_VALUE;
        for(int i=1; i<n; i++){
            res = Math.min(res, Math.max(minPages(arr, i, k-1), sum(arr, i, n)));
        }
        return res;
    }

    private static int sum(int[] arr, int i, int n) {
        int sum = 0;
        for(int k=i; k<n; k++){
            sum += arr[k];
        }
        return sum;
    }
}
