package com.problem;

public class RemoveDuplicate {

  static void main() {
    int[] nums = {1, 1, 2, 3, 3, 4, 4, 5, 5};
    ListNode head = cretaeLinkedList(nums);
    printList(head);
    ListNode result = deleteDuplicates(head);
    printList(result);
  }

  private static void printList(ListNode head) {
    while (head != null) {
      System.out.print(head.val + " -> ");
      head = head.next;
    }
    System.out.println();
  }

  private static ListNode cretaeLinkedList(int[] nums) {
    ListNode dummy = new ListNode(0);
    ListNode prev = dummy;
    for (int num : nums) {
      prev.next = new ListNode(num);
      prev = prev.next;
    }
    prev.next = null;
    return dummy.next;
  }


  public static ListNode deleteDuplicates(ListNode head) {
    ListNode curr = head;
    ListNode dummy = head;
    while (curr != null) {
      if (curr.val == head.val) {
        curr = curr.next;
      } else {
        head.next = curr;
        head = head.next;
        curr = curr.next;
      }
    }
    if (head != null) {
      head.next = curr;
    }
    return dummy;
  }

  static class ListNode {

    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }

    ListNode(int x) {
      val = x;
    }
  }
}
