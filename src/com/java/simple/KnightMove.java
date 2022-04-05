package com.java.simple;

public class KnightMove {
    public static void main(String[] args) {
        BlackAndWhite blackAndWhite = new BlackAndWhite();
        System.out.println(blackAndWhite.numOfWays(2, 3));
    }
}

class BlackAndWhite {
    //Function to find out the number of ways we can place a black and a
    //white Knight on this chessboard such that they cannot attack each other.
    public long numOfWays(int N, int M) {
        long mod = 1000000007;
        long val = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                long totalCell = N * M - 1;
                boolean[][] vis = new boolean[N][M];
                knightStep(i, j, vis, N, M);
                val += totalCell  % mod;
            }
        }
        return val;
    }

    public void knightStep(int i, int j, boolean[][] vis,
                           int N, int M) {

        /* j column*/
        int[] x = {2, 2, 1, -1, -2, -2, -1, 1};
        /* i row */
        int[] y = {1, -1, -2, -2, 1, -1, 2, 2};

        if (i < 0 || j < 0 || i >= N || j >= M || vis[i][j]) {
            return;
        }

        if (i >= 0 && i < N && j >= 0 && j < M && !vis[i][j]) {
            vis[i][j] = true;
            for (int z = 0; z < 8; z++) {
                int a = i + y[z];
                int b = j + x[z];
                if (a < N && b < M) {
                    knightStep(a, b, vis, N, M);
                    i = i - y[z];
                    j = j - x[z];
                }

            }
        }
    }
}
