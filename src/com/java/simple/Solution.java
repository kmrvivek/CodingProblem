package com.java.simple;

class Solutionleet {
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 6};
        int target = 2;
        System.out.println(searchInsert(arr, target));
    }
    
    public static int searchInsert(int[] arr, int target) {

        int low = 0, high = arr.length-1;
        while(low < high){
            int mid = low + (high-low)/2;
            if(arr[mid] == target){
                return mid;
            }
            if(arr[mid] > target){
                high = mid-1;
            } else {
                low = mid+1;
            }
        }
        return low+1;
    }
}