package com.java.problems;

public class NetCoreSubstring {

    public static void main(String[] args) {
        String st = "AABCDCEE";
        String tar = "ABCE";
        System.out.println(minWindowSubSequence(st, tar).length());
    }

    private static String minWindowSubSequence(String s, String t){
        if(s.length() == 0 || t.length() == 0){
            return "";
        }
        int right = 0;
        int minLength = Integer.MAX_VALUE;
        String minSub = "";
        while (right < s.length()){
            int tidx = 0;
            while(right < s.length()){
                if(s.charAt(right) == t.charAt(tidx)){
                    tidx++;
                }

                if(tidx == t.length()){
                    break;
                }
                right++;
            }
            if(right == s.length()){
                break;
            }
            int left = right;
            tidx = t.length()-1;
            while(left >= 0){
                if(s.charAt(left) == t.charAt(tidx)){
                    tidx--;
                }
                if(tidx < 0){
                    break;
                }
                left--;
            }
            if(right - left+1 < minLength){
                minLength = right-left+1;
                minSub = s.substring(left, right+1);
            }
            right = left+1;
        }
        return minSub;
    }
}
