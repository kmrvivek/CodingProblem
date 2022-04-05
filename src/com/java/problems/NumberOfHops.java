package com.java.problems;

public class NumberOfHops {

    public static void main(String[] args) {
        NumberOfHops numberOfHops = new NumberOfHops();
        System.out.println(numberOfHops.countNoOfHops(1));
        System.out.println(numberOfHops.countNoOfHops(4));
        int[] arr = {3, 5, 10};
        System.out.println(numberOfHops.score(arr, 8, arr.length));
        System.out.println(numberOfHops.score(arr, 20, arr.length));
        System.out.println(numberOfHops.scoreDp(8));
        System.out.println(numberOfHops.scoreDp(20));
    }

    private int countNoOfHops(int dest){
        if(dest == 0){
            return 1;
        }
        if(dest < 0){
            return 0;
        }
        return countNoOfHops(dest-1) + countNoOfHops(dest-2) + countNoOfHops(dest-3);
    }

    private int scoreDp(int n){
        int[][] dp = new int[4][n+1];
        for(int i=0; i<3; i++){
            dp[i][0] = 1;
        }
        int[] arr = {3, 5, 10};
        for(int i=0; i<3; i++){
            for(int j=1; j<=n; j++){
                if(j >= arr[i]) {
                    dp[i][j] += dp[i][j - arr[i]];
                }
            }
        }
        return dp[2][n];
    }

    private int score(int arr[], int score, int n){
        if(score == 0){
            return 1;
        }

        if(n <= 0){
            return 0;
        }
        int res = score(arr, score, n-1);
        if(score >= arr[n-1]){
            res += score(arr, score-arr[n-1], n);
        }
        return res;
    }
}
