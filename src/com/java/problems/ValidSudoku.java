package com.java.problems;

public class ValidSudoku {

    public static void main(String[] args) {
        char[][] board = 	{{'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}};

        ValidSudoku validSudoku = new ValidSudoku();
        System.out.println(validSudoku.isValidSudoku(board));

        int[][] a = {{1}};
        System.out.println(validSudoku.searchMatrix(a, 1));

        ListNode node = new ListNode(1);
        System.out.println(validSudoku.hasCycle(node));

        String s = "abcabcbb";
        System.out.println(validSudoku.lengthOfLongestSubstring(s));

    }
    private boolean isValidSudoku(char[][] board) {
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!isRowValid(i, board, j) || !isColValid(i, board, j)
                        || !isGridValid(i, board, j)){
                    return false;
                }
            }
        }
        return true;
    }


    private boolean isRowValid(int r, char[][] board, int c){
        for(int i=0; i<9; i++){
            if(i !=c && board[r][i] != '.'){
                if(board[r][i] == board[r][c]){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isColValid(int r, char[][] board, int c){
        for(int i=0; i<9; i++){
            if(i !=r && board[i][c] != '.'){
                if(board[i][c] == board[r][c]){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGridValid(int r, char[][] board, int c){
        int row = r/3;
        int col = c/3;

        row = row*3;
        col = col*3;

        int endRow = row+3;
        int endCol = col+3;
        for(int i=row; i<endRow; i++){
            for(int j=col; j<endCol; j++){
                if(i !=r && j != c && board[i][j] != '.'){
                    if(board[i][j] == board[r][c]){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int row = getRowNumber(matrix, target);
        if(row == -1){
            return false;
        }
        for(int i=0; i<matrix[0].length; i++){
            if(target == matrix[row][i]){
                return true;
            }
        }
        return false;
    }

    public int getRowNumber(int[][] matrix, int target){
        int j=matrix[0].length-1;
        for(int i=0; i<matrix.length; i++){
            if(target <= matrix[i][j]){
                return i;
            }
        }
        return -1;
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while(slow != null && fast != null){
            if(slow == fast){
                return true;
            } else {
                slow = slow.next;
                fast = fast.next.next;
            }
        }
        return false;
    }

    public int lengthOfLongestSubstring(String s) {

        int[] arr = new int[26];
        int maxLen = 0;
        int start = 0;
        for(int i=0; i<s.length(); i++){
            if(arr[s.charAt(i)-'a'] == 0){
                arr[s.charAt(i)-'a']++;
                maxLen = Math.max(maxLen, i-start+1);
            } else {
                while(arr[s.charAt(i)-'a'] > 0 && start< s.length()){
                    arr[s.charAt(start)-'a']--;
                    start++;
                }
            }
        }
        return maxLen;
    }

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }
}
