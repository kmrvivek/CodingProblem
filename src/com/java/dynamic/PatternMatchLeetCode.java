package com.java.dynamic;

public class PatternMatchLeetCode {

    public static void main(String[] args) {
        String s = "aab";
        String p = "c*a*b";
        System.out.println(isMatch(s, p));
    }

    private static boolean isMatch(String s, String p) {
         /* case 1 :- s[i] == p[j] || p[j] == '.' => true
        dp[i][j] = d[i-1][j-1];
        case 2 :- p[j] = '*'
            2.1 s[i] == p[j-2] => true (#* -> empty)
            dp[i][j] = dp[i][j-2];
            s[i] == p[j-1] => true
              2 case ->
               2.1 s[i-1] == p[j] => we know i and j-1 matches
               so if we repeat p[j-1] n times to solve s[i] =>
               dp[i][j] = dp[i-1][j]

        */

        if (s == null || p == null) {
            return false;
        }
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        char[] string = new char[s.length() + 1];
        char[] pattern = new char[p.length() + 1];
        // we don't care about what character is on index 0.
        // It is a way to make our view of index in dp[][] and string[] and pattern[] consistent.
        // Our process starts at index==1 for string and pattern.
        string[0] = (char) 0;
        pattern[0] = (char) 0;
        for (int i = 0; i < s.length(); ++i) {
            string[i+1] = s.charAt(i);
        }
        for (int i = 0; i < p.length(); ++i) {
            pattern[i+1] = p.charAt(i);
        }

        dp[0][0] = true;
        // initialization of dp, for base cases
        for (int i = 1; i < pattern.length; ++i) {
            // here we assume that pattern[1] (i.e. p.charAt(0)) will never be '*' based on the
            // description of the problem which states that '*' matches zero or more of the
            // preceding element, so it must have a preceding element otherwise it is not a valid input.
            if (pattern[i] == '*' && dp[0][i-2]) {
                dp[0][i] = true;
            }
        }

        for (int i = 1; i < string.length; ++i) {
            for (int j = 1; j < pattern.length; ++j) {
                if (pattern[j] == string[i] || pattern[j] == '.') {
                    //  we just need to know if previous sequence matches or not
                    dp[i][j] = dp[i-1][j-1];
                } else if (pattern[j] == '*') {
                    if (pattern[j-1] != string[i] && pattern[j-1] != '.') {
                        //we should treat pattern[j-1...j] as matching nothing in string,
                        // it is valid because pattern[j] == '*', therefore we want to know if
                        // string[1...i] matches pattern[1...j-2]
                        dp[i][j] = dp[i][j-2];
                    } else {
                        // for dp is true: either pattern[j-1...j] matches string[i], or pattern[j-1...j] matches nothing in string
                        // if pattern[j-1] matches string[i], we just need to know if string[i-1] matches pattern[j]: abc* matches abc and abcc, if we want to know if abc matches abc*, we just need to know if ab matches abc*; if we want to know if abcc matches abc*, we just need to know if abc matches abc*
                        dp[i][j] = dp[i-1][j] || dp[i][j-2];
                    }
                }
            }
        }
        return dp[s.length()][p.length()];

    }
}
