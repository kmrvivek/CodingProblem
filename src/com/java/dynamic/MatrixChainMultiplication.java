package com.java.dynamic;

public class MatrixChainMultiplication {

    public static void main(String[] args) {
        int[] arr = {20,10, 30,40};
        System.out.println(getMinMultiplication(arr, 0, arr.length-1));
        System.out.println(dpMatrixMutiplication(arr, arr.length));
    }

    private static int getMinMultiplication(int[] arr, int i, int j){
        if(i+1 == j){
            return 0;
        }

        int res = Integer.MAX_VALUE;
        for(int k=i+1; k<j; k++){
            int firstHalfSet = getMinMultiplication(arr, i, k);
            int secondHalfSet = getMinMultiplication(arr, k, j);
            int sum = arr[i] * arr[k] * arr[j];
            res = Math.min(res, firstHalfSet+secondHalfSet+sum);
        }
        return res;
    }

    private static int dpMatrixMutiplication(int[] arr, int n){
        int[][] dp = new int[n][n];
        for(int gap=2; gap < n; gap++){
            for(int i=0; i+gap < n; i++){
                int j = i+gap;
                dp[i][j] = Integer.MAX_VALUE;
                for(int k=i+1; k<j; k++){
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + arr[i]*arr[k]*arr[j]);
                }
            }
        }
        return dp[0][n-1];
    }
}
