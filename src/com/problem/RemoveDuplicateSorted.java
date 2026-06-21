package com.problem;

import java.util.Arrays;

public class RemoveDuplicateSorted {

  static void main() {
    int[] nums = {1, 1, 2, 3, 3, 4, 4, 5, 5};
    int n = removeDuplicates(nums);
    System.out.println("Length of the array after removing duplicates: " + n);
    System.out.println("Array after removing duplicates: " + Arrays.toString(nums));
    int[] nums2 = {1, 1, 2, 3, 3, 4, 4, 5, 5};
    int[] nums3 = {1, 1, 1, 2, 2, 3};
    int[] nums4 = {0, 0, 1, 1, 1, 1, 2, 3, 3};
    int[] nums5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    printArray(removesDuplicatesTwice(nums2), nums2);
    printArray(removesDuplicatesTwice(nums3), nums3);
    printArray(removesDuplicatesTwice(nums4), nums4);
    printArray(removesDuplicatesTwice(nums5), nums5);
  }

  private static void printArray(int n, int[] nums) {
    for (int i = 0; i < n; i++) {
      System.out.print(nums[i] + " ");
    }
    System.out.println();
  }

  static int removeDuplicates(int[] nums) {
    int i = 0, j = 1;
    while (j < nums.length) {
      if (nums[i] != nums[j]) {
        nums[++i] = nums[j];
      }
      j++;
    }
    return i + 1;
  }

  static int removesDuplicatesTwice(int[] nums) {
    int i = 0, j = 0;
    while (j < nums.length) {
      if (i < 2 || nums[i - 2] != nums[j]) {
        nums[i++] = nums[j];
      }
      j++;
    }
    return i;
  }

  public static int[] sortedSquares(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      nums[i] = nums[i] * nums[i];
    }
    int[] arr = new int[nums.length];
    int i = 0, j = nums.length - 1, k = nums.length - 1;
    while (k >= 0) {
      if (nums[i] > nums[j]) {
        arr[k--] = nums[i++];
      } else {
        arr[k--] = nums[j--];
      }
    }
    return arr;
  }
}
