package com.java.dynamic;

import java.util.Arrays;

public class Fibonacci {
    private  int[] memo;
    public static void main(String[] args) {
        Fibonacci fibonacci = new Fibonacci();
        int n = 5;
        fibonacci.memo = new int[n+1];
        Arrays.fill(fibonacci.memo,-1);
        int val = fibonacci.fib(n);
        System.out.println(val);
        System.out.println(Arrays.toString(fibonacci.memo));
    }


    public int fib(int n){
        if(memo[n] == -1){
            int res;
            if(n == 0 || n == 1){
                res = n;
            } else {
                res = fib(n-1) + fib(n-2);
            }
            memo[n] = res;
        }
        return memo[n];
    }
}
