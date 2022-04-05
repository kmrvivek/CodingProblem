package com.java.simple;

import java.util.concurrent.TimeUnit;

public class BackTrack {

    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.findMaximumNum("1034", 2));
    }
}

class Solution {
    static String maxstr;

    //Function to find the largest number after k swaps.
    public String findMaximumNum(String str, int k) {
        maxstr = str;
        char[] c = str.toCharArray();
        recurse(c, k);
        System.out.println((System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30)) / 1000L);
        return maxstr;
    }

    public void recurse(char[] str, int k) {
        if (k == 0) {
            return;
        }
        int n = str.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (str[i] < str[j]) {
                    char ch = str[i];
                    str[i] = str[j];
                    str[j] = ch;
                    if (String.valueOf(str).compareTo(maxstr) > 0) {
                        maxstr = String.valueOf(str);
                    }
                    recurse(str, k - 1);
                    char chd = str[i];
                    str[i] = str[j];
                    str[j] = chd;
                }
            }
        }
    }
}
