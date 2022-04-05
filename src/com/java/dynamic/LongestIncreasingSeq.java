package com.java.dynamic;

import java.util.ArrayList;
import java.util.List;

public class LongestIncreasingSeq {

    public static void main(String[] args) {
        int arr[] = {0,8,4,12,2,10,6,14,1,9,5,13,3,11,7,15};

        int nums[] = {5,8,3,7,9,1};

        System.out.println(longestSubsequence(arr.length, arr));
        System.out.println(longestSubsequenceLogn(arr.length, nums));
    }

    static int longestSubsequence(int size, int a[])
    {
        int lis[] = new int[size];
        lis[0] = 1;
        for(int i=1; i<size; i++){
            lis[i] = 1;
            for(int j=0; j<i; j++){
                if(a[j] < a[i]){
                    lis[i] = Math.max(lis[i], lis[j]+1);
                }
            }
        }
        int max = 0;
        for(int i=1; i<size; i++){
            if(lis[i] > max){
                max = lis[i];
            }
        }
        return max;
    }

    private static int longestSubsequenceLogn(int size, int a[])
    {
        List<Integer> nums = new ArrayList<>();
        nums.add(a[0]);
        for(int i=1; i<size; i++){
            if(nums.get(nums.size()-1) < a[i]){
                nums.add(a[i]);
            } else {
                int ceil = findCeil(nums, a[i], 0, nums.size());
                nums.set(ceil, a[i]);
            }
        }
        return nums.size();
    }

    private static int findCeil(List<Integer> nums, int val, int l, int r){
        while(l<r){
            int mid = l + (r -l)/2;
            if(nums.get(mid) >= val){
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return r;
    }
}
