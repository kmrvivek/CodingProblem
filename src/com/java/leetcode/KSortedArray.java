package com.java.leetcode;

public class KSortedArray {
    /*
     To find pivot element,
     1) do the binary search if mid > mid+1, return pivot
     2) Choose the element if Lower side if low > mid => high = mid
     3) Choose the element to right side if mid > low => low = mid +1
     */


    public static void main(String[] args) {
        //int[] arr = {4,5,6,7,0,1,2};
        int[] arr = {1};
        int target = 1;
        int pivot = findPivotElement(arr, 0, arr.length-1);
        System.out.println("Pivot "+pivot);
        if(arr[0] <= target  && arr[pivot] >= target){
            System.out.println(binarySearch(arr, 0, pivot, target));
        } else {
            System.out.println(binarySearch(arr, pivot+1, arr.length-1, target));
        }

        System.out.println(searchSinglePass(arr, target));
    }

    private static int findPivotElement(int[] arr, int low, int high){
        while(low<high){
            int mid = low + (high-low)/2 ;
            if(arr[mid] > arr[mid+1]){
                return mid;
            }
            if(arr[low] < arr[mid]){
                low = mid+1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private static int binarySearch(int[] arr, int low, int high, int target){
        while(low<=high){
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

        return -1;
    }

    /*
        Think like if in the mid if you think the left to mid is in strictly increasing
        then do the search in left to mid if the target lies in the range 
        otherwise it will lie on the unsorted place now again if you repeat the process each time you will 
        know one part where it will be strictly increasing and other not and you have to search
     */
    private static int searchSinglePass(int[] nums, int target) {

        int n=nums.length;
        int low = 0;
        int high = n-1;

        while(low <= high){
            int mid = low + (high-low)/2;
            if(nums[mid] == target){
                return mid;
            }

            if(nums[mid] >= nums[low]){
                if(target >= nums[low] && target < nums[mid]){
                    high = mid-1;
                } else {
                    low = mid+1;
                }
            } else {
                if(target > nums[mid] && target <= nums[high]){
                    low = mid+1;
                } else {
                    high = mid-1;
                }
            }
        }
        return -1;
    }
}
