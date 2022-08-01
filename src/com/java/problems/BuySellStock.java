package com.java.problems;

import java.util.PriorityQueue;
import java.util.Queue;

public class BuySellStock {

    public static void main(String[] args) {
        int[] arr = {1,2,4,2,5,7,2,4,9,0};
        BuySellStock buySellStock = new BuySellStock();
        System.out.println(buySellStock.maxProfit(arr));
    }

    private int maxProfit(int[] arr) {

        Queue<Integer> q = new PriorityQueue<>((o1, o2)-> {
            return o2-o1;
        });

        int buy = arr[0];
        int sell = arr[0];
        int profit = 0;

        for(int i=1; i<arr.length; i++){
            if(arr[i-1] < arr[i]){
                sell = arr[i];
            } else {
                q.add(arr[i-1]-buy);
                buy = arr[i];
                sell = arr[i];
            }
        }
        q.add(sell-buy);
        return q.poll()+q.peek();

    }
}
