package com.java.leetcode;

import java.util.ArrayList;
import java.util.List;

public class FindAnagram {

    public static void main(String[] args) {
        String s = "baa";
        String p = "aa";
        System.out.println(findAnagrams(s, p));
    }

        private static List<Integer> findAnagrams(String s, String p) {
            List<Integer> ls = new ArrayList<>();
            if(p.length() > s.length()){
                return ls;
            }

            int[] arr = new int[26];
            int[] a = new int[26];
            int count = 0;
            for(int i=0; i<p.length(); i++){
                arr[p.charAt(i)-'a']++;
                a[p.charAt(i)-'a']++;
            }

            for(int i=0; i<26; i++){
                if(arr[i] > 0){
                    count++;
                }
            }
            int org = count;

            for(int i=0; i<=s.length()-p.length(); i++){
                for(int j=i; j<i+p.length(); j++){
                    if(a[s.charAt(j)-'a'] > 0){
                        a[s.charAt(j)-'a']--;

                        if(a[s.charAt(j)-'a'] == 0){
                            count --;
                        }
                    }
                    if(count == 0){
                        ls.add(i);
                    }
                }
            }

            return ls;
        }

}
