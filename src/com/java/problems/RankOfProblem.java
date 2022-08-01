package com.java.problems;

import java.util.ArrayList;
import java.util.List;

public class RankOfProblem {

    public static void main(String[] args) {
        int[] arr = {4, 1, 3, 2};
        int[] arr1 = {5, 1, 1, 7, 2};
        System.out.println(imbalanceRank(arr));
        System.out.println(imbalanceRank(arr1));
    }

    private static int imbalanceRank(int[] arr){
        int rank = 0;
        for(int i=2; i<=arr.length; i++){
            for(int j=0; j<= arr.length-i; j++){
                List<Integer> curList = new ArrayList<>(i);
                rank += addSubset(curList, j, j+i, arr);
            }
        }
        return rank;
    }

    private static int addSubset(List<Integer> curList, int i, int j, int[] arr) {
        int sum = 0;
        int low = arr[i];
        for(int k=i; k<j; k++){
            curList.add(arr[k]);
            sum += arr[k];
            if(arr[k] < low){
                low = arr[k];
            }
        }
        System.out.println(curList);
       int val =  sumOfAP(low, 1, curList.size());
        if(sum > val){
            return 1;
        } else {
            return 0;
        }
    }



    private static int findImbalance(List<Integer> list){
        int sum = 0;
        for(int i =0; i<list.size(); i++){
            sum += list.get(i);
        }
        return sum;
    }

    static int sumOfAP(int a, int d, int n)
    {
        double mf =  (double)n / 2;
        double sum =  (2 * a + (n - 1) * d);
        sum = sum * mf;
        return (int)sum;
    }
}
