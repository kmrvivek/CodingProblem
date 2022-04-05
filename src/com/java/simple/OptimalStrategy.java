package com.java.simple;

public class OptimalStrategy {

    public static void main(String[] args) {
        int[] arr = {2, 3, 25, 7};
        int n = arr.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        System.out.println(getOptimalValueSum(arr, 0, n - 1, sum));
        System.out.println(getOptimalValue(arr, 0, n - 1));
    }

    private static int getOptimalValueSum(int[] arr, int i, int j, int sum) {

        if (j == i + 1) {
            return Math.max(arr[i], arr[j]);
        }

        int x = sum - getOptimalValueSum(arr, i + 1, j, sum - arr[i]);
        int y = sum - getOptimalValueSum(arr, i, j - 1, sum - arr[j]);
        return Math.max(x, y);
    }

    private static int getOptimalValue(int[] arr, int i, int j) {
        if (j == i + 1) {
            return Math.max(arr[i], arr[j]);
        }
        return Math.max(arr[i] + Math.min(getOptimalValue(arr, i + 2, j),
                        getOptimalValue(arr, i + 1, j - 1)),
                arr[j] +
                        Math.min(getOptimalValue(arr, i + 1, j-1),
                                getOptimalValue(arr, i, j - 2)));
    }
}
