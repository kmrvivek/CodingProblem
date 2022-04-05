package com.java.problems;

public class MinPathSum {
    public static void main(String[] args) {
        int[][] grid = {{1,2,3},{4,5,6}};
        MinPathSum minPathSum = new MinPathSum();
        System.out.println(minPathSum.doDfs(grid, grid.length, grid[0].length));
    }

    private int doDfs(int[][] grid, int row, int col){
        int[][] dp = new int[row][col];
        for(int i=1; i<row; i++){
            dp[i][0] += dp[i-1][0];
        }
        for(int i=1; i<col; i++){
            dp[0][i] += dp[0][i-1];
        }
        /*for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                System.out.print(dp[]);
            }
        }*/
        for(int i=1; i<row; i++){
            for(int j=1; j<col; j++){
                dp[i][j] = Math.min(grid[i-1][j], dp[i][j-1]);
            }
        }
        return dp[row-1][col-1];
    }
}
