package com.java.problems;

public class MinCoinChange {

    public static void main(String[] args) {
        int[] coins = {2, 5, 3, 6};
        int value = 10;
        int numOfCoins = 4;
        System.out.println(minCoins(coins, numOfCoins, value));
    }

    private static int minCoins(int[] coins, int numOfCoins, int value){
        if(value == 0){
            return 0;
        }
        int res = Integer.MAX_VALUE;
        for(int i=0; i<numOfCoins; i++) {
            if (value >= coins[i]) {
               int subRes = minCoins(coins, numOfCoins, value-coins[i]);
               if(subRes != Integer.MAX_VALUE){
                   res = Math.min(res,1+ subRes);
               }
            }
        }
        return res;
    }

}
