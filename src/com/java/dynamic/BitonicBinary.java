package com.java.dynamic;

import java.util.Arrays;

public class BitonicBinary {

    public static void main(String[] args) {
        int arr[] = {1, 11, 2, 10, 4, 5, 2, 1};
        System.out.println(getLBSLengthLogn(arr));
    }

    static void reverseArr(int arr[]) {
        int i = 0;
        int j = arr.length - 1;
        while (i < j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }
    }

    static int getLBSLengthLogn(int arr[]) {
        int n = arr.length;

        // Base Case:
        if (n == 0)
            return 0;
        int increasing[] = new int[n];
        int tail1[] = new int[n];

        int decreasing[] = new int[n];
        int tail2[] = new int[n];
        increasing[0] = arr[0];
        int in = 1;
        tail1[0] = 0;
        for (int i = 1; i < n; i++) {
            if (arr[i] < increasing[0]) {
                increasing[0] = arr[i];
                tail1[i] = 0;
            } else if (arr[i] > increasing[in - 1]) {
                increasing[in++] = arr[i];
                tail1[i] = in - 1;
            } else {
                int getIndex1 = Arrays.binarySearch(increasing,
                        0, in, arr[i]);
                if (getIndex1 <= -1)
                    continue;

                increasing[getIndex1] = arr[i];
                tail1[i] = getIndex1;
            }
        }
        in = 1;

        reverseArr(arr);
        decreasing[0] = arr[0];
        tail2[0] = 0;

        for (int i = 0; i < n; i++) {
            if (arr[i] < decreasing[0]) {
                decreasing[0] = arr[i];
                tail2[i] = 0;
            } else if (arr[i] > decreasing[in - 1]) {
                decreasing[in++] = arr[i];
                tail2[i] = in - 1;
            } else {
                int getIndex2 = Arrays.binarySearch(decreasing,
                        0, in, arr[i]);
                if (getIndex2 <= -1)
                    continue;

                decreasing[getIndex2] = arr[i];
                tail2[i] = getIndex2;

            }
        }
        reverseArr(arr);
        reverseArr(tail2);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (ans < (tail1[i] + tail2[i] + 1))
                ans = (tail1[i] + tail2[i] + 1);
        }
        return ans;
    }
}
