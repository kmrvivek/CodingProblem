package com.java.problems;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DisappearingNumbersNetCore {

    public static void main(String[] args) {
        int[] a = { 1, 2, 3, 4, 5 };
        int[][] input = { { 1, 4 }, { 6, 1 }, { 3, 5 } };
        performOperation(a, input);
    }

    private static void performOperation(int[] a, int[][] input){
        List<Integer> res = new ArrayList<>();
        int n = a.length;
        for(int i=0; i<input.length; i++){
            int time = input[i][0];
            int pos = input[i][1];

            if(pos > n){
                res.add(-1);
                continue;
            }
            int rotation = time/n;
            int rem = time % n;
            // Even rotation
            if(rotation % 2 == 0){
                Queue<Integer> q = new LinkedList<>();
                int j = 0;
                while(j < a.length){
                    q.add(a[j]);
                    j++;
                }
                int poppedNum = rem;
                while(poppedNum > 0){
                    q.poll();
                    poppedNum--;
                }
                if(q.size() < pos){
                    res.add(-1);
                } else {
                    int k = pos;
                    while(k > 1){
                        q.poll();
                        k--;
                    }
                    res.add(q.poll());
                }
            } else {
                Queue<Integer> q = new LinkedList<>();
                int addNum = 0;
                while(addNum < rem){
                    q.add(a[addNum++]);
                }
                if(q.size() < pos){
                    res.add(-1);
                } else {
                    int k = pos;
                    while(k > 1){
                        q.poll();
                        k--;
                    }
                    res.add(q.poll());
                }
            }
        }
        System.out.println(res);
    }
}
