package com.java.problems;

public class LeetCode {

    public static void main(String[] args) {
        int arr[] = {1,2,3,4,5};
        ListNode ls = new ListNode(-1);
        ListNode head = ls;
        for(int i=0; i< arr.length; i++){
            ls.next = new ListNode(arr[i]);
            ls = ls.next;
        }
        head = head.next;
       ListNode al =  reverseListRec(head);
        System.out.println(al);

        int[][] mat = {{0,0,0},{0,1,0},{1,1,1}};
        LeetCode leetCode = new LeetCode();
        System.out.println(leetCode.updateMatrix(mat));
    }

    public int[][] updateMatrix(int[][] mat) {
        int[][] vis = new int[mat.length][mat[0].length];
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat[0].length; j++){
                if(mat[i][j] == 1){
                    vis[i][j] = 100000;
                    doDFS(mat, i, j, vis, i, j);
                }
            }
        }

        return vis;

    }

    public int doDFS(int[][] mat, int sr, int sc, int[][] vis, int l, int m){
        if((sr < 0 || sc < 0 || sr >= mat.length || sc >= mat[0].length) || mat[sr][sc] == 1){
            return 100000;
        }
        if(mat[sr][sc] == 0){
            return 0;
        }

        int[] row = {0, 0, -1, 1};
        int[] col = {-1, 1, 0, 0};
        for(int i=0; i<4; i++){
            int r = row[i] + sr;
            int c = col[i] + sc;

                vis[l][m] = Math.min(vis[l][m], 1+doDFS(mat, r, c, vis, l, m));
        }
        return 100000;
    }

    public boolean isValid(int row, int col, int[][] image){
        if(row >= 0 && row < image.length && col >=0 && col < image[0].length){
            return true;
        }
        return false;
    }

    public static ListNode reverseList(ListNode head) {
        ListNode newHead = null;
        while(head != null){
            ListNode next = head.next;
            ListNode ls = head;
            ls.next = newHead;
            head = next;
            newHead = ls;
        }
        return newHead;

    }

    public static ListNode reverseListRec(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        ListNode ls = reverseListRec(head.next);
        head.next.next = head;
        head.next = null;
        return  ls;
    }



    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }


}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
