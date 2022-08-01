package com.java.problems;

import java.util.*;

public class MergeSortedLinkedLists {

    public static void main(String[] args) {
        int[][] arr = {{1,4,5},{1,3,4},{2,6}};
        //int[][] arr = {{2},{},{-1}};
        ListNode[] lls = new ListNode[arr.length];
        for(int i=0; i< arr.length; i++){
            int[] a = arr[i];
            int j=0;
            ListNode ls = new ListNode(-1);
            ListNode al = ls;
            while(j< a.length){
                ls.next = new ListNode(a[j++]);
                ls = ls.next;
            }
            lls[i] = al.next;
        }

        ListNode res = mergeKListQueue(lls);
        while(res != null){
            System.out.print(res.val +" -> ");
            res = res.next;
        }

    }

    private static ListNode mergeKLists(ListNode[] listsd) {

        List<ListNode> lists = new ArrayList(Arrays.asList(listsd));
        ListNode temp = new ListNode(-1);
        ListNode head = temp;

        while(!lists.isEmpty()){
            int min = Integer.MAX_VALUE;
            int pos =-1;

            for(int i=0; i<lists.size(); i++){
                if(lists.get(i) != null && lists.get(i).val < min){
                    min = lists.get(i).val;
                    pos = i;
                }
            }
            if(min != Integer.MAX_VALUE && pos != -1){
                temp.next = new ListNode(min);
                temp = temp.next;
                ListNode ls = lists.get(pos);
                lists.remove(pos);
                lists.add(ls.next);
            }

            for(int i=0; i<lists.size(); i++){
                if(lists.get(i) == null){
                    lists.remove(i);
                }
            }
        }
        return head.next;
    }


    private static ListNode mergeKListQueue(ListNode[] listsd){
        Queue<ListNode> q = new PriorityQueue<ListNode>((o1, o2) -> {
            if(o1 != null && o2 != null){
               return o1.val-o2.val;
            } else {
                return -1;
            }
        });

        for(int i=0; i<listsd.length; i++){
            if(listsd[i] != null)
            q.add(listsd[i]);
        }

        ListNode ls = new ListNode(-1);
        ListNode al = ls;
        while(!q.isEmpty()){
            ListNode l = q.poll();
            ls.next = new ListNode(l.val);
            ls = ls.next;
            if(l.next != null){
                l = l.next;
                q.add(l);
            }
        }
        return al.next;
    }


    static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }
}
