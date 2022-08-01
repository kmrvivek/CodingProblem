package com.java.simple;

import java.util.*;
import java.util.stream.Collectors;

public class ReverseStack {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        Stack<Integer> st = new Stack<>();
        for(int i=0; i<arr.length; i++){
            st.push(arr[i]);
        }
        System.out.println(st);
        reveseStack(st);
        System.out.println(st);
        Object[][] input = {{'a', 10}, {'b', 20}, {'c', 15}, {'d', 5}};
        sortHashMap(input);
    }

    private static void sortHashMap(Object[][] input) {
        Map<Character, Integer> mp = new HashMap<>();
        for(int i=0; i<input.length; i++){
            mp.put((Character) input[i][0], (Integer) input[i][1]);
        }
        List<Map.Entry<Character, Integer>> ls = new ArrayList<>(mp.entrySet());
        Collections.sort(ls, (o1, o2) -> o1.getValue()- o2.getValue());
        System.out.println(ls);
        List<Map.Entry<Character, Integer>> als = new ArrayList<>(mp.entrySet());
        Collections.sort(als, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return o1.getValue() -o2.getValue();
            }
        });
        System.out.println(als);
        Map<Character, Integer> linkMap =
        mp.entrySet().stream().sorted(((o1, o2) -> o1.getValue().compareTo(o2.getValue())))
                .collect(Collectors.toMap(Map.Entry:: getKey, Map.Entry:: getValue,
                        (v1, v2)-> v1, LinkedHashMap::new));
        System.out.println(linkMap);

    }

    private static void reveseStack(Stack<Integer> st) {

        if(st.isEmpty()){
            return;
        }
        int val = st.pop();
        reveseStack(st);
        insertAtEnd(st, val);
    }

    private static void insertAtEnd(Stack<Integer> st, int val) {
        if(st.isEmpty()){
            st.push(val);
        } else {
            int item = st.pop();
            insertAtEnd(st, val);
            st.add(item);
        }
    }
}
