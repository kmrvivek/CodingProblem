package com.java.problems;

public class SubsetProblem {

    public static void main(String[] args) {
        int[] arr = {2, 3, 5, 6, 8, 10, 7};
        int sum = 10;
        System.out.println(getCountSubset(arr, 0, sum));
        int[] arr1 = {1,5,11,5};
        int[] arr2 = {1, 3, 5, 5};
        System.out.println(getEqualSum(arr1, 22, 0));
        System.out.println(getEqualSum(arr2, 7, 0));
    }

    private static int getCountSubset(int[] arr, int i, int sum) {
        if(sum == 0){
            return 1;
        }
        if(i == arr.length){
            return  0;
        }
        int res = 0;
        if(arr[i] <= sum){
            res += getCountSubset(arr, i+1 , sum-arr[i]);
        }
        res += getCountSubset(arr, i+1, sum);
        return res;
    }

    private static boolean getEqualSum(int arr[], int sum, int i){
        if(sum == 0){
            return true;
        }
        if(i == arr.length){
            return false;
        }
        if(arr[i] <= sum){
            return getEqualSum(arr, sum, i+1) || getEqualSum(arr, sum-arr[i], i+1);
        }
        return getEqualSum(arr, sum , i+1);
    }
}
