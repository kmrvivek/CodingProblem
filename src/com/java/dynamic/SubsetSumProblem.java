package com.java.dynamic;

import java.util.ArrayList;
import java.util.List;

public class SubsetSumProblem {

    public static void main(String[] args) {
        int[] arr = {2, 5, 3};
        int sum = 5;
        List<List<Integer>> res = new ArrayList<>();
        subset(arr, 0, new ArrayList<>(), res);
        System.out.println(res);
        System.out.println(getSubSetSum(arr, arr.length, sum));
        System.out.println(subSetSumDP(arr, sum));
    }

    private static int getSubSetSum(int[] arr, int n, int sum){
        if(n == 0){
            return sum == 0 ? 1 : 0;
        }
        return getSubSetSum(arr, n-1, sum) + getSubSetSum(arr, n-1, sum-arr[n-1]);
    }

    private static void subset(int[] arr, int index, List<Integer> temp, List<List<Integer>> res) {
        res.add(new ArrayList<>(temp));
        for (int i = index; i < arr.length; i++) {
            temp.add(arr[i]);
            subset(arr, i + 1, temp, res);
            temp.remove(temp.size() - 1);
        }
    }

    private static int subSetSumDP(int[] arr, int sum){
        int[][] dp = new int[arr.length+1][sum+1];
        for(int i=0; i<= arr.length; i++){
            dp[i][0] = 1;
        }

        for(int i=1; i<= arr.length; i++){
            for(int j=1; j<=sum; j++){
                if(j<arr[i-1]){
                    dp[i][j] = dp[i-1][j];
                } else {
                    dp[i][j] = dp[i-1][j] + dp[i-1][j-arr[i-1]];
                }
            }
        }
        return dp[arr.length][sum];
    }
}
