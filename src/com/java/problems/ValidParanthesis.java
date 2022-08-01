package com.java.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidParanthesis {

    public static void main(String[] args) {
        int n = 3;
        ValidParanthesis validParanthesis = new ValidParanthesis();
        System.out.println(validParanthesis.generateParenthesis(n));
    }

    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<String>();
        backtrack(list, "", 0, 0, n);
        return list;
    }

    public void backtrack(List<String> list, String str, int open, int close, int max){

        if(str.length() == max*2){
            list.add(str);
            return;
        }

        if(open < max)
            backtrack(list, str+"(", open+1, close, max);
        if(close < open)
            backtrack(list, str+")", open, close+1, max);

    }
}
