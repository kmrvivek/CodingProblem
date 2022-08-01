package com.java.problems;

public class LongestPalindromicString {

    public static void main(String[] args) {
        String str = "ababccdccbab";
        longestPalindromicSubString(str);
    }

    private static void longestPalindromicSubString(String str){
        int start = 0, end = 0;
        for (int i=0; i<str.length(); i++){
            int len1 = expandPalindromicString(str, i, i+1);
            int len2 = expandPalindromicString(str, i, i);
            int len = Math.max(len1, len2);
            if(end-start < len){
                start = i-(len-1)/2;
                end = i+len/2;
                System.out.println("start "+ start +" - End "+end+" -Len "+len+" -i "+i);
            }
        }
        System.out.println("Max Palindrome : "+str.substring(start, end+1));
    }

    private static int expandPalindromicString(String str, int i, int j) {
        while(i>=0 && j<str.length() && str.charAt(i) == str.charAt(j)) {
            i--;
            j++;
        }
        return j-i-1;
    }
}
