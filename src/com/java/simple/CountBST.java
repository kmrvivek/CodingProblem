package com.java.simple;

public class CountBST {

    public static void main(String[] args) {
        System.out.println(getCount(5));
    }
    private static int getCount(int n) {
        if (n == 0) {
            return 1;
        }
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            int left = getCount(i - 1) ;
            int right = getCount(n - i);
            sum += left * right;
        }
        return sum;
    }
}
