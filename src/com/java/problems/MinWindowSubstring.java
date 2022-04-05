package com.java.problems;

import java.util.HashMap;
import java.util.Map;

public class MinWindowSubstring {
    public static void main(String[] args) {
       String s = "ADOBECODEBANC", t = "ABC";
        MinWindowSubstring minWindowSubstring = new MinWindowSubstring();
        System.out.println(minWindowSubstring.minWindow(s, t));
        System.out.println(minWindowSubstring.minWindowRas(s, t));
    }
    public String minWindow(String s, String t) {

        Map<Character, Value> mp = new HashMap<>();

        for(int i=0; i<t.length(); i++){
            Character c = t.charAt(i);
            Value val;
            if(mp.containsKey(c)){
                val = mp.get(c);
                val.input = val.input+1;
            } else {
                val = new Value();
                val.input = 1;
            }
            mp.put(c,val);
        }

       int start = -1, end = -1, size = mp.size(), minLength = Integer.MAX_VALUE;
        String res = "";
        boolean order = false;
        for(int i=0; i<s.length(); i++){
            Character c = s.charAt(i);
            end = i;
            if(mp.containsKey(c)){
                if(start == -1){
                    start = i;
                }
                if(!order && size > 0 && mp.get(c).input != mp.get(c).target){
                    mp.get(c).target++;
                    if(mp.get(c).input == mp.get(c).target){
                        size--;
                    }
                } else if(order && size > 0 && mp.get(c).target != 0){
                    mp.get(c).target--;
                    if(mp.get(c).target == 0){
                        size--;
                    }
                }
            }
            if(size == 0){
                if(start > -1 && end - start >= 0 && end-start < minLength){
                    res = s.substring(start, end+1);
                    minLength = res.length();
                }

                start = -1;
                size = mp.size();
                order = !order;
            }
        }
        return res;
    }

    private String minWindowRas(String s, String t) {

        HashMap<Character, Integer> map = new HashMap<>();

        for(Character c : t.toCharArray()){
            map.put(c, map.getOrDefault(c,0)+1);
        }

        int matchCount = map.size();

        int begin=0 , end =0;
        int len = Integer.MAX_VALUE;
        int start=0;
        while(end<s.length()){
            char in = s.charAt(end);
            if(map.containsKey(in)){
                map.put(in, map.get(in)-1);
                if(map.get(in)==0)
                    matchCount--;
            }
            end++;

            while(matchCount == 0){
                char out = s.charAt(begin);
                if(map.containsKey(out)){
                    map.put(out, map.get(out)+1);
                    if(map.get(out)>0)
                        matchCount++;
                }

                if(end-begin < len){
                    len = end-begin;
                    start = begin;
                }
                begin++;
            }
        }
        if(len ==  Integer.MAX_VALUE)
            return "";
        else
            return s.substring(start, start+len);
    }
}

class Value{
    int target;
    int input;
}
