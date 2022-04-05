package com.java.problems;

public class WaysNSum {

    public static void main(String[] args) {
        System.out.println(countWays(4, 5));
        System.out.println(countStairs(2, 4));
        System.out.println(countWays(4));
        System.out.println(countUniqueStairs(new int[]{1, 2}, 0, 5));
        System.out.println(countStairsUqDp(new int[]{1, 2}, 2, 4));
    }

    static int countUniqueStairs(int arr[], int size, int n) {
        // unbounded knapsack coin change sum
       /* if (n == 0) {
            return 1;
        }
        if(size == 0){
            return  0;
        }
        return countUniqueStairs(arr, size-1, n) + countUniqueStairs(arr, size, n-arr[size-1]);*/
        if (n == 0) {
            return 1;
        }
        if (size == 2) {
            return 0;
        }
        int res = 0;
        if (arr[size] <= n) {
            res += countUniqueStairs(arr, size, n - arr[size]);
        }
        res += countUniqueStairs(arr, size + 1, n);
        return res;
    }

    static int countStairsUqDp(int[] arr, int m, int n) {
        int[] table = new int[n + 1];
        table[0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = arr[i]; j <= n; j++) {
                table[j] += table[j - arr[i]];
            }
        }
        return table[n];
    }

    static int countWays(int n) {
        int mod = (int) (1e9 + 7);
        int[][] dp = new int[3][n + 1];
        for (int i = 0; i < 3; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i][j - 1] % mod;
                if (i <= j) {
                    dp[i][j] = dp[i][j] % mod + dp[i][j - i] % mod;
                }
            }
        }
        return dp[2][n] % mod;
    }

    private static int countStairs(int n, int value) {
        if (value == 0) {
            return 1;
        }
        if (value < 0) {
            return 0;
        }
        return countStairs(n, value - 1) + countStairs(n, value - 2);
    }

    private static int countWays(int n, int value) {
        if (value == 0) {
            return 1;
        }
        if (n == 0) {
            return 0;
        }
        int res = countWays(n - 1, value);
        if (n <= value) {
            res = res + countWays(n, value - n);
        }
        return res;
    }
}
