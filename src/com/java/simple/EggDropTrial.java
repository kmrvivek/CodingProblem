package com.java.simple;

public class EggDropTrial {

    public static void main(String[] args) {
        System.out.println(getMinTrials(10, 1));
        System.out.println(getMinTrials(10, 2));
    }


    private static int getMinTrials(int floors, int eggs){
        if(eggs == 1 || floors == 1 || floors == 0){
            return floors;
        }
        int min = Integer.MAX_VALUE;
        for (int i=1; i<=floors; i++){
            int eggBreak = getMinTrials(i-1, eggs-1);
            int eggSolid =getMinTrials(floors-i, eggs);
            int val = Math.max(eggBreak, eggSolid) + 1;
            min = Math.min(min, val);
        }
        return min;
    }
}
