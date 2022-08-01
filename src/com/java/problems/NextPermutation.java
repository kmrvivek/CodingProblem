package com.java.problems;

import java.util.Arrays;

public class NextPermutation {

    /*
        Next permutation tells the next bigger number
        1) Take the last peak value means i-1 < i
        2) Now check if the value previous from peak is less than peak value after peak
        3) so that will be our last peak swap the number and after swapping arrange in asc
        order so that rest number will be min
     */

    public static void main(String[] args) {
        int a[] = {100,99,98,97,96,95,94,93,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
        System.out.println(Arrays.toString(nextPermutation(a)));
    }

    private static int[] nextPermutation(int[] arr){
        int i=1;
        int lastPeak = -1;
        while(i<arr.length){
            if(arr[i] > arr[i-1]){
                lastPeak = i;
            }
            i++;
        }
        // this means this is descending order, the number is greatest that can be formed
        if(lastPeak == -1){
            for (int j=0; j<arr.length/2; j++){
                int temp = arr[j];
                arr[j] = arr[arr.length-1-j];
                arr[arr.length-1-j] = temp;
            }
        }

        int val = arr[lastPeak];
        int index = lastPeak;
        for(int k=lastPeak; k<arr.length; k++){
            if(arr[k] > arr[lastPeak-1] && arr[k] < arr[index]){
                index = k;
            }
        }
        int temp = arr[index];
        arr[index] = arr[lastPeak-1];
        arr[lastPeak-1] = temp;
        Arrays.sort(arr, lastPeak, arr.length);
        return arr;
    }
}
