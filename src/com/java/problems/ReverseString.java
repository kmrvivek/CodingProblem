package com.java.problems;

public class ReverseString {

    public static void main(String[] args) {
        String input = "Welcome   to $ Saharanpur";
        char[] str = input.toCharArray();
        ReverseString reverseString = new ReverseString();
        System.out.println(reverseString.reverseString(str, str.length));
    }

    private String reverseString(char[] str, int n) {
        int start = 0;
        int end = 0;
        for (end=0; end<n; end++){
            if (' ' == str[end]) {
                reverseWord(str,start, end-1);
                start = end+1;
            }
        }
        reverseWord(str, start, n-1);
        reverseWord(str, 0, n-1);
        return new String(str);
    }

    private void reverseWord(char[] str, int start, int end) {
        while(start < end){
            char a = str[start];
            str[start] = str[end];
            str[end] = a;
            start++;
            end--;
        }
    }
}
