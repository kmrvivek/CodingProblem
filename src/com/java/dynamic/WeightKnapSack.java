package com.java.dynamic;

public class WeightKnapSack {

    public static void main(String[] args) {
        int[] val = {10, 40, 30, 50};
        int[] wt = {5, 4, 6, 3};
        int W = 10;
        int[] val1 = {60, 100, 120};
        int[] wt1 = {10, 20, 30};
        int W1 = 50;
        System.out.println(getMaxWeight(val, wt, W, val.length));
        System.out.println(getMaxWeight(val1, wt1, W1, val1.length));
        System.out.println(getMaxWeightDp(val, wt, W, val.length));
        System.out.println(getMaxWeightDp(val1, wt1, W1, val1.length));

    }

    private static int getMaxWeightDp(int[] val, int[] wt, int W, int n){
        int memo[][] = new int[n+1][W+1];
        for(int i=1; i<=n; i++){
            for(int j=1; j<=W; j++){
                if(wt[i-1] > j){
                    memo[i][j] = memo[i-1][j];
                } else {
                    memo[i][j] = Math.max(memo[i-1][j-wt[i-1]]+ val[i-1], memo[i-1][j]);
                }
            }
        }
        return memo[n][W];
    }

    private static int getMaxWeight(int[] val, int[] wt, int W, int n){

        if(n == 0 || W == 0){
            return 0;
        }
       if(wt[n-1] > W){
           return getMaxWeight(val, wt, W, n-1);
       } else{
           int x = getMaxWeight(val, wt, W-wt[n-1], n-1) + val[n-1];
           int y = getMaxWeight(val, wt, W, n-1);
           return Math.max(x, y);
          /* return Math.max(getMaxWeight(val, wt, W, n-1) , val[n-1] +
                   getMaxWeight(val, wt, W-wt[n-1], n-1));*/
       }
    }
}
