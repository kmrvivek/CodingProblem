package com.java.simple;

public class Soduku {
    static int[][] grid =
            {{3, 0, 6, 5, 0, 8, 4, 0, 0},
             {5, 2, 0, 0, 0, 0, 0, 0, 0},
             {0, 8, 7, 0, 0, 0, 0, 3, 1},
             {0, 0, 3, 0, 1, 0, 0, 8, 0},
             {9, 0, 0, 8, 6, 3, 0, 0, 5},
             {0, 5, 0, 0, 9, 0, 6, 0, 0},
             {1, 3, 0, 0, 0, 0, 2, 5, 0},
             {0, 0, 0, 0, 0, 0, 0, 7, 4},
             {0, 0, 5, 2, 0, 6, 3, 0, 0}};

    public static void main(String[] args) {
        System.out.println(Soduku.SolveSudoku(grid));
        Soduku.printGrid(grid);
    }

    static boolean SolveSudoku(int grid[][]) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    for (int num = 1; num <= grid.length; num++) {
                        if (isSafe(i, j, num, grid)) {
                            grid[i][j] = num;
                            if (SolveSudoku(grid) == true) {
                                return true;
                            }
                            grid[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //Function to print grids of the Sudoku.
    static void printGrid(int grid[][]) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static boolean isSafe(int i, int j, int currVal, int grid[][]) {

        for (int r = 0; r < grid.length; r++) {
            if (grid[r][j] == currVal || grid[i][r] == currVal) {
                return false;
            }
        }
        int s = (int) Math.sqrt(grid.length);
        int rs = i - (i % s);
        int cs = j - (j % s);
        for (int r = 0; r < s; r++) {
            for (int c = 0; c < s; c++) {
                if (grid[r + rs][c + cs] == currVal) {
                    return false;
                }
            }
        }
        return true;
    }
}
