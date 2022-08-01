package com.designPattern.designPatternPrac.LeetcodeProblems;

import java.util.*;


public class ThrotllingGateway_Cisco {
    public static void main(String[] args) {

        int[] arr = new int[]{1,1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7};
        List<Integer> requestTimes = new ArrayList<>();
        for(int a : arr)
            requestTimes.add(a);
        int drop = droppedRequests(requestTimes);
        System.out.println("DropperdRequest are : "+drop);

    }

    public static int droppedRequests(List<Integer> requestTime){
        int requestDrop = 0;
        Queue<Integer> requestQ01 = new LinkedList<Integer>();
        Queue<Integer> requestQ10 = new LinkedList<Integer>();
        Queue<Integer> requestQ60 = new LinkedList<Integer>();

        for(int i=0; i< requestTime.size(); i++){


            int currRequest = requestTime.get(i);

            // check current time and the time in 10s

            int currTime = currRequest;
            int preTime01 = Math.max(currTime -1, 1);
            int preTime10 = Math.max(currTime - 9, 1);
            int preTime60 = Math.max(currTime - 59, 1);

            boolean drop01=false, drop10=false, drop60=false;

            //while requestQ01 contains any request not in current second, remove it
            while( requestQ01.peek()!=null && requestQ01.peek() < currTime){
                System.out.println(" Q01 remove item: " + requestQ01.peek());
                requestQ01.poll();

            }

            //while requestQ10 contains any request not in past 10 seconds, remove it
            while( requestQ10.peek()!=null && requestQ10.peek() < preTime10 ){
                System.out.println(" Q10 remove item: " + requestQ10.peek());
                requestQ10.poll();
            }

            //while requestQ60 contains any request not in past 60 second, remove it
            while( requestQ60.peek()!=null && requestQ60.peek() < preTime60){
                System.out.println(" Q60 remove item: " + requestQ60.peek());
                requestQ60.poll();
            }

            /*****************
             * Although, if a request is to be dropped due to multiple violations, it is still counted only once.
             */

            //add current request to each Queue;
            requestQ01.add(currRequest);
            requestQ10.add(currRequest);
            requestQ60.add(currRequest);

            //check size of each Queue, if the count of total items exceed the limit, drop++;
            if(requestQ01.size() > 3){
                drop01 = true;
                System.out.println("* Drop 01 ");
            }

            if(requestQ10.size() > 20){
                drop10 = true;
                System.out.println("* Drop 10 ");
            }

            if(requestQ60.size() > 60){
                drop60 = true;
                System.out.println("* Drop 60 ");
            }

            // if an item got dropped from any Queue, requestDrop ++;
            if(drop01 || drop10 || drop60){
                requestDrop ++;
            }

        }//end for loop

        return requestDrop;

    }

}
