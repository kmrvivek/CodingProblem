package com.java.problems;

import javafx.util.Pair;

import java.util.*;

public class TwillioChess {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
        int[] weigh = {3, 2, 1, 4};
        int n = 4;
        int k = 3;
        Queue<Pair<Integer, Integer>> q = new LinkedList<>();
        for(int i=0; i<n; i++){
            q.add(new Pair<>(arr[i], weigh[i]));
        }
        System.out.println(findWinnerKthGames(q, k));
    }

    private static Pair<Integer, Integer> findWinnerKthGames(Queue<Pair<Integer, Integer>> q, int k) {
        if(k == 0){
            return q.peek();
        }
        while(k-- > 0){
            Iterator<Pair<Integer, Integer>> itr = q.iterator();
            Pair<Integer, Integer> player1 = itr.next();
            Pair<Integer, Integer> player2 = itr.next();
            if(player1.getValue() > player2.getValue()){
                q.remove(player2);
                q.add(player2);
            } else {
                q.remove(player1);
                q.add(player1);
            }
            System.out.println(q);
        }
        return q.peek();
    }


}
