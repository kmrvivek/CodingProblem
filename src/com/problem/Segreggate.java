package com.problem;

import java.util.Arrays;

public class Segreggate {

  static void main() {
    int[] arr = {0, 1, 0, 1, 0, 0, 1, 1, 1, 0};
    Segreggate segreggate = new Segreggate();
    segreggate.segregate0and1(arr);
    System.out.println(Arrays.toString(arr));
  }

  void segregate0and1(int[] arr) {
    int i = 0, j = arr.length - 1;
    while (i <= j) {
      if (arr[i] == 0) {
        i++;
      } else if (arr[j] == 1) {
        j--;
      } else {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        i++;
        j--;
      }
    }
  }

}
