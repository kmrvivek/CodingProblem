package com.java.simple;

import java.util.ArrayList;
import java.util.Arrays;

public class UniqueSubset {
    public static void main(String[] args) {
        int[] arr = {2, 1, 3, 4};
        Solve solve = new Solve();
        ArrayList<ArrayList<Integer>> res = solve.AllSubsets(arr, arr.length);
        System.out.println(res);
    }
}

class Solve {

    public ArrayList<ArrayList<Integer>> AllSubsets(int arr[], int n) {
        ArrayList<ArrayList<Integer>> finalRes = new ArrayList<>();
        Arrays.sort(arr);
        ArrayList<Integer> res = new ArrayList<>();
        subset(0, arr, res, n, finalRes);
        //finalRes.remove(0);
        return finalRes;
    }

    private void subset(int index, int arr[], ArrayList<Integer> res, int n,
                        ArrayList<ArrayList<Integer>> finalRes) {


            finalRes.add(new ArrayList<Integer>(res));
        for (int i = index; i < n; i++) {
            int b = arr[i];
            res.add(b);
            subset(i + 1, arr, res, n, finalRes);
            res.remove(new Integer(b));
        }
        return;
    }
}