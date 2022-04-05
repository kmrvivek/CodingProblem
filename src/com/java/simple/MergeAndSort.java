package com.java.simple;

import java.util.ArrayList;
import java.util.List;

public class MergeAndSort {

    public static void main(String[] args) {
        int[] arr1 = {20, 10, 40, 30};
        int[] arr2 = {60, 90, 50, 80, 70};
        Node linkList1 = createLinkedList(arr1);
        printLinkList(linkList1);
        Node linkList2 = createLinkedList(arr2);
        printLinkList(linkList2);
        List<Node> allLinkList = new ArrayList<>();
        allLinkList.add(linkList1);
        allLinkList.add(linkList2);
        Node head = mergeAllLinkList(allLinkList);
        printLinkList(head);
        LinkedListMergeSort linkedListMergeSort = new LinkedListMergeSort();
        Node result = linkedListMergeSort.mergeSort(head);
        printLinkList(result);
    }

    private static Node mergeAllLinkList(List<Node> allLinkList){
        Node head = allLinkList.get(0);
        Node temp = head;
        for(int i=0; i<allLinkList.size()-1; i++){
                while(temp.next != null){
                    temp = temp.next;
                }
                temp.next = allLinkList.get(i+1);
        }
        return head;
    }

    private static void printLinkList(Node root){
        while(root != null){
            System.out.print(root.value+" -> ");
            root = root.next;
        }
        System.out.println();
    }
    private static Node createLinkedList(int[] arr){
        Node head = new Node(arr[0]);
        Node temp = head;
        for(int i=1; i< arr.length; i++){
            temp.next = new Node(arr[i]);
            temp = temp.next;
        }
        return head;
    }


}

class Node {
    int value;
    Node next;

    public Node(int value){
        this.value = value;
        next = null;
    }
}

class LinkedListMergeSort {
    public Node mergeSort(Node head){
        if(head == null || head.next == null){
            return head;
        }
        Node middle = getMiddle(head);
        Node nextToMiddle = middle.next;
        middle.next = null;
        Node left = mergeSort(head);
        Node right = mergeSort(nextToMiddle);
        Node sortedList = merge(left, right);
        return sortedList;
    }

    private Node merge(Node left, Node right){
        Node result = null;
        if(left == null){
            return right;
        }
        if(right == null){
            return left;
        }
        if(left.value <= right.value){
            result = left;
            result.next = merge(left.next, right);
        } else {
            result = right;
            result.next = merge(left, right.next);
        }
        return result;
    }

    private Node getMiddle(Node head){
        if(head == null){
            return null;
        }
        Node slow = head;
        Node fast = head;
        while(fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

}
