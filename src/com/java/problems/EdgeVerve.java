package com.java.problems;

public class EdgeVerve {

    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 2 , 10};
        EdgeVerve edgeVerve = new EdgeVerve();
        edgeVerve.getMinValue(arr);
    }

    private void getMinValue(int[] arr){
        int min = Integer.MIN_VALUE;
        int pos = 0;
        for(int i=0; i< arr.length-1; i++){
            int val = arr[i] - arr[i+1];
            int res = val * val;
            if(res > min){
                min = res;
                pos = i+1;
            }
        }
       int num = (arr[pos-1] + arr[pos])/2;
        System.out.println("Min Value equal distant "+num);
    }
}
