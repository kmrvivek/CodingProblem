package com.java.dynamic;

public class MinNumSteps {
    public static void main(String[] args) {
        int[] arr = {4, 1, 5, 3, 1, 3, 2, 1, 8};

        //int[] arr = {1,4,3,2,6,7};

        MinNumSteps minNumSteps = new MinNumSteps();
        System.out.println(minNumSteps.minimumJumps(arr, 0, arr[0]));
    }

    private int minJumpDP(int[] arr, int n) {
        if (arr.length <= 1)
            return 0;
        if (arr[0] == 0)
            return -1;
        int jump = 1;
        int max = arr[0];
        int step = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (i == arr.length - 1)
                return jump;
            max = Math.max(max, i + arr[i]);
            step--;
            if (step == 0) {
                jump++;
                if (i >= max)
                    return -1;
                step = max - i;
            }
        }
        return -1;
    }

    private int minimumJumps(int arr[], int a, int n) {
        if (n >= arr.length - 1) {
            return 1;
        }
        int jump = Integer.MAX_VALUE;
        for (int i = a; i <= n; i++) {
            if (i + arr[i] <= arr.length) {
                int subJump = minimumJumps(arr, i + 1, i + arr[i]);
                if (subJump != Integer.MAX_VALUE) {
                    jump = Math.min(subJump + 1, jump);
                }
            }
        }
        return jump;
    }
}
