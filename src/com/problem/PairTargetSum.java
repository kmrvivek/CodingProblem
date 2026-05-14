package com.problem;

public class PairTargetSum {

  static void main() {
    int[] numbers = {2, 7, 11, 15};
    int target = 9;

    int[] result = twoSum(numbers, target);

    System.out.println(
        "The indices of the two numbers that add up to " + target + " are: " + result[0] + " and "
            + result[1]);
  }

  public static int[] twoSum(int[] numbers, int target) {

    int i = 0, j = numbers.length - 1;
    while (i <= j) {
      if (numbers[i] + numbers[j] == target) {
        return new int[]{i, j};
      } else if (numbers[i] + numbers[j] < target) {
        i++;
      } else {
        j--;
      }
    }
    return new int[]{-1, -1};
  }
}
