package com.java.simple;

public class KadaneDivideAndConquer {

    public static void main(String[] args) {
        int[] arr = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArraySum(arr, 0, arr.length-1));
    }

    /*
    Using Divide and Conquer approach, we can find the maximum subarray sum in O(nLogn) time.
    Following is the Divide and Conquer algorithm.

        Divide the given array in two halves
        Return the maximum of following three
        Maximum subarray sum in left half (Make a recursive call)
        Maximum subarray sum in right half (Make a recursive call)
        Maximum subarray sum such that the subarray crosses the midpoint
     */

    /*
    Maximum subarray sum such that the subarray crosses the midpoint:-

      find the maximum sum starting from mid point and ending at some point on left of mid,
      then find the maximum sum starting from mid + 1 and ending with some point on right of mid + 1.
      Finally, combine the two and return the maximum among left, right and combination of both.
     */

    private static int midPointSubArray (int[] arr, int l, int h, int mid){
        int sum = 0;
        int left_sum = Integer.MIN_VALUE;
        for(int i=mid; i>=l; i--){
            sum += arr[i];
            if(sum > left_sum){
                left_sum = sum;
            }
        }

        sum = 0;
        int right_sum = Integer.MIN_VALUE;
        for(int i=mid+1; i<=h; i++){
            sum += arr[i];
            if(sum > right_sum){
                right_sum = sum;
            }
        }
        // Return sum of elements on left
        // and right of mid
        // returning only left_sum + right_sum will fail for
        // [-2, 1]
        return Math.max(left_sum+right_sum, Math.max(left_sum, right_sum));
    }

    private static int maxSubArraySum(int[] arr, int l, int h){
        if(l>=h){
            return arr[l];
        }
        int mid = l + (h-l)/2;
        return Math.max(midPointSubArray(arr, l, h, mid)
                ,Math.max(maxSubArraySum(arr, l, mid), maxSubArraySum(arr,mid+1, h)));
    }
}
