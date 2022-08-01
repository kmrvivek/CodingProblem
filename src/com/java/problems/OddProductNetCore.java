package com.java.problems;

public class OddProductNetCore {

    public static void main(String[] args) {
        int[] arr = {5, 1, 2, 3, 4};
        System.out.println(getOddProductCount(arr));
    }

    private static int getOddProductCount(int[] arr){
        int count = 0;
        int evenIndex = -1;
        //continous odd array num
        int k = 0;
        for(int i=0; i<arr.length; i++){
            //check for even index and from start it will give between continuous odd number
            if(arr[i] % 2 == 0){
                 k = i-evenIndex-1;
                 //number of count of permutation oder not matter
                count += k * (k+1)/2;
                evenIndex = i;
            }
        }
        // check number of odd from last number
        k = arr.length - evenIndex -1;
        count += k * (k+1)/2;
        return count;
    }


}
