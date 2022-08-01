package com.java.problems;

import java.util.*;

public class LetterCombinations {
    private static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) {
        String digits = "23";
        map.put(2, "abc");
        map.put(3, "def");
        map.put(4, "ghi");
        map.put(5, "jkl");
        map.put(6, "mno");
        map.put(7, "pqrs");
        map.put(8, "tuv");
        map.put(9, "xyz");
        System.out.println(letterCombinations(digits));
    }

    private static List<String> letterCombinations(String digits) {
       List<String> res = new ArrayList<>();
       permuteString("", digits, 0, res);
       return res;
    }

    private static void permuteString(String prefix, String digits, int point, List<String> res) {
        if(point >= digits.length()){
            res.add(prefix);
            return;
        }
        String letters = map.get(Integer.valueOf(digits.charAt(point)-'0'));
        for(int i=0; i<letters.length(); i++){
            permuteString(prefix+letters.charAt(i), digits, point+1, res);
        }
    }


}
