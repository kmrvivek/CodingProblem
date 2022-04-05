package com.java.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinSelectionRecursion {
    static List<Integer> subRes = new ArrayList<>();
    static List<List<Integer>> finalRes = new ArrayList<>();

    public static void main(String[] args) {
        int[] coins = {5, 7};
        int sum = 13;
        //{1, 1, 1, 1}, {1, 1, 2}, {2, 2}, {1, 3}

        coinSelect(coins, coins.length, sum, 0);
        System.out.println(finalRes);
        numberOfWays(coins, coins.length, sum);
    }

    public static void coinSelect(int[] arr, int n, int sum, int i) {
        if (sum < 0) {
            return;
        }
        if (sum == 0) {
            finalRes.add(new ArrayList<>(subRes));
            return;
        }
        if (i == n) {
            return;
        }

        if (sum >= arr[i]) {
            subRes.add(arr[i]);
            coinSelect(arr, n, sum - arr[i], i);
            subRes.remove(subRes.size() - 1);
            coinSelect(arr, n, sum, i + 1);
        }
    }

    public static long numberOfWays(int coins[],int numberOfCoins,int value)
    {
        //creating an array for storing number of ways.
        long ways[]=new long[value+1];

        //initializing number of ways at 0 to 1.
        ways[0]=1;

        //running a loop on the list of coin denominations.
        for(int coin:coins)
        {
            //loop for values
            for(int i=1;i<value+1;i++)
            {
                //if coin value is less than or equal to ith value.
                if(i>=coin)
                {
                    //updating number of ways at ith
                    //index as ways[i]+ways[i-coin].
                    ways[i]+=ways[i-coin];
                }
            }
        }
        System.out.println(Arrays.toString(ways));
        //returning the number of ways.
        return ways[value];
    }

}
