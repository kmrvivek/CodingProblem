package com.java.problems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

public class MaxFrequency {
/*
    Given an array of n numbers and a positive integer k. The problem is to find k
    numbers with most occurrences, i.e., the top k numbers having the maximum frequency.
    If two numbers have the same frequency then the larger number should be given preference.
    The numbers should be displayed in decreasing order of their frequencies. It is assumed
    that the array consists of k numbers with most occurrences.
    Input:
    arr[] = {3, 1, 4, 4, 5, 2, 6, 1},
    k = 2
    Output: 4 1
    Explanation:
    Frequency of 4 = 2
    Frequency of 1 = 2
    These two have the maximum frequency and 4 is larger than 1.

    Input :
    arr[] = {7, 10, 11, 5, 2, 5, 5, 7, 11, 8, 9},
    k = 4
    Output: 5 11 7 10
    Explanation:
    Frequency of 5 = 3
    Frequency of 11 = 2
    Frequency of 7 = 2
    Frequency of 10 = 1
    These four have the maximum frequency and 5 is largest among rest. */

    public static void main(String[] args) {
        int[] arr1 = {3, 1, 4, 4, 5, 2, 6, 1};
        int k1 = 2;
        int[] arr2 = {7, 10, 11, 5, 2, 5, 5, 7, 11, 8, 9};
        int k2 = 4;
        MaxFrequency maxFrequency = new MaxFrequency();
        maxFrequency.getMaxFrequency(arr1, k1);
        System.out.println("\n");
        maxFrequency.getMaxFrequency(arr2, k2);

        //2nd way using list
        System.out.println("\n ArrayList method -> ");
        maxFrequency.getListMaxFrequency(arr1, k1);
        System.out.println("\n");
        maxFrequency.getListMaxFrequency(arr2, k2);
    }

    private void getListMaxFrequency(int[] arr, int k){
        List<NumberFrequency> ls = new ArrayList<>();
        Map<Integer, Integer> num = new HashMap<>();
        for(int i=0; i< arr.length; i++){
            if(num.containsKey(arr[i])){
                num.put(arr[i], num.get(arr[i])+1);
            } else {
                num.put(arr[i], 1);
            }
        }
        num.entrySet().forEach(n -> ls.add(new NumberFrequency(n.getKey(), n.getValue())));
        ls.sort(new Comparator<NumberFrequency>() {
            @Override
            public int compare(NumberFrequency o1, NumberFrequency o2) {
                if(o1.frequency != o2.frequency){
                    return o2.getFrequency() - o1.getFrequency();
                } else {
                    return o2.getNumber() - o1.getNumber();
                }
            }
        });
        for(int i=0; i<k; i++){
            System.out.println(ls.get(i).getNumber() +" -> "+ ls.get(i).getFrequency());
        }
    }

    private void getMaxFrequency(int[] arr, int k){
        PriorityQueue<NumberFrequency> pq = new PriorityQueue<>(new Comparator<NumberFrequency>() {
            @Override
            public int compare(NumberFrequency o1, NumberFrequency o2) {
                if(o1.frequency != o2.frequency){
                    return o2.getFrequency() - o1.getFrequency();
                } else {
                    return o2.getNumber() - o1.getNumber();
                }

            }
        });
        Map<Integer, Integer> num = new HashMap<>();

        for(int i=0; i< arr.length; i++){
            if(num.containsKey(arr[i])){
                num.put(arr[i], num.get(arr[i])+1);
            } else {
                num.put(arr[i], 1);
            }
        }
        num.entrySet().forEach(n -> pq.add(new NumberFrequency(n.getKey(), n.getValue())));
        for(int i=0; i<k; i++){
            System.out.println(pq.poll());
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NumberFrequency {
        int number;
        int frequency;
    }
}


