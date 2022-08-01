package com.java.problems;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ShortestWindowSubString {
    public static void main(String[] args) throws IOException {
        String S = "ADOBECODEBANC";
        String T = "ABC";
        System.out.println(minWindow(S, T));
        System.out.println(mappedWindow(S, T));
    }


    public static String minWindow(String S, String T) {
        char[] s = S.toCharArray();
        char[] t = T.toCharArray();
        int m[] = new int[26];

        // Answer
        int ans = Integer.MAX_VALUE; // length of ans
        int start = 0; // starting index of ans
        int count = 0;
        // creating map
        for (int i = 0; i < t.length; i++) {
            if (m[t[i] - 'A'] == 0)
                count++;
            m[t[i] - 'A']++;
        }

        // References of Window
        int i = 0;
        int j = 0;

        // Traversing the window
        while (j < s.length) {
            Character c = s[j];
            // Calculations
            m[s[j] - 'A']--;
            if (m[s[j] - 'A'] == 0)
                count--;

            // Condition matching
            if (count == 0) {
                while (count == 0) {

                    // Sorting ans
                    if (ans > j - i + 1) {
                        ans = j - i + 1;
                        start = i;
                    }

                    // Sliding I
                    // Calculation for removing I
                    m[s[i] - 'A']++;
                    if (m[s[i] - 'A'] > 0)
                        count++;

                    i++;
                }
            }
            j++;
        }

        if (ans != Integer.MAX_VALUE)
            return String.valueOf(s).substring(start, ans + start);
        else
            return "";
    }

    private static String mappedWindow(String s, String t) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character c : t.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int count = map.size();
        int start = 0, begin = 0, end = 0;
        int ans = Integer.MAX_VALUE;
        while (end < s.length()) {
            char in = s.charAt(end);
            if (map.containsKey(in)) {
                map.put(in, map.get(in) - 1);
                if (map.get(in) == 0) {
                    count--;
                }
            }
            end++;
            while (count == 0) {
                char out = s.charAt(begin);
                if (map.containsKey(out)) {
                    map.put(out, map.get(out) + 1);
                    if (map.get(out) > 0) {
                        count++;
                    }
                }

                if (end - begin < ans) {
                    ans = end - begin;
                    start = begin;
                }
                begin++;
            }

        }
        if (ans != Integer.MAX_VALUE) {
            return s.substring(start, start + ans);
        } else {
            return "-1";
        }
    }
}