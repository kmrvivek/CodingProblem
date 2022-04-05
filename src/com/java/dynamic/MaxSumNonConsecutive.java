package com.java.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaxSumNonConsecutive {

    public static void main(String[] args) {
        List<List<Integer>> arr = new ArrayList<>();
        arr.add(Arrays.asList(10, 5, 15, 20));
        arr.add(Arrays.asList(1, 10, 2));
        arr.add(Arrays.asList(8, 7, 6, 10));
        arr.add(Arrays.asList(10, 5, 15, 20, 2, 30));
        arr.add(Arrays.asList(10, 20, 30, 40, 50));
        arr.forEach(integers -> {
            int n = integers.size();
            Integer[] a = integers.toArray(new Integer[0]);
            System.out.println(getMaxSumNonCons(a, n));
            getMaxSumNonConsDp(a, n);
        });
    }

    private static void getMaxSumNonConsDp(Integer[] arr, int n){
        int[] dp = new int[n+1];
        dp[1] = arr[0];
        dp[2] = Math.max(arr[0], arr[1]);
        for (int i=3; i<=n; i++){
            dp[i] = Math.max(dp[i-1], dp[i-2]+arr[i-1]);
        }
//        System.out.println(Arrays.toString(dp));
        System.out.println(dp[n]);
    }

    private static int getMaxSumNonCons(Integer[] arr, int n) {
        if (n == 1) {
            return arr[0];
        } else if (n == 2) {
            return Math.max(arr[0], arr[1]);
        } else {
            return Math.max(getMaxSumNonCons(arr, n - 1), getMaxSumNonCons(arr, n - 2) + arr[n - 1]);
        }
    }
}
