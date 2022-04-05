package com.java.simple;

import java.util.ArrayList;
import java.util.List;


public class CoinSubset {

    private static  List<List<Integer>> finalRes = new ArrayList<>();
    public static void main(String[] args) {
       int[] coins = {1, 2, 3};
       int sum = 4;
       List<Integer> res =new ArrayList<>();
        getCount(coins, coins.length, sum, res);
        System.out.println(finalRes);
        System.out.println(getCountSum(coins, coins.length, sum));
    }


    public static void getCount(int[] coins, int n, int sum,  List<Integer> res){
        if(sum == 0){
           List<Integer> subRes = new ArrayList(res);
            finalRes.add(subRes);
            return;
        }
        if(n == 0){
            return;
        }
        getCount(coins, n-1, sum, res);
        if(coins[n-1] <= sum){
            res.add(coins[n-1]);
           getCount(coins, n, sum-coins[n-1], res);
          res.remove(new Integer(coins[n-1]));
        }
    }

    public static int getCountSum(int[] coins, int n, int sum){
        if(sum == 0){
            return 1;
        }
        if(n == 0){
            return 0;
        }
        int res = getCountSum(coins, n-1, sum);
        if(coins[n-1] <= sum){
            res = res + getCountSum(coins, n, sum-coins[n-1]);
        }
        return  res;
    }
}

