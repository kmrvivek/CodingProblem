package com.java.simple;

import java.util.ArrayList;
import java.util.List;

public class GraphColor {

    public static void main(String[] args) {
        int[][] K = {{1, 3, 2}, {0, 2}, {1, 3, 0}, {2, 0}};
        int[] color = new int[K.length];
        GraphSolve solve = new GraphSolve();
        List<Integer>[] G = new ArrayList[K.length];
        for (int i=0; i < K.length; i++){
            List<Integer> al = new ArrayList<>();
            for(int j=0; j<K[i].length; j++){
                al.add(K[i][j]);
            }
            G[i] = al;
        }

        GraphSolve graphSolve = new GraphSolve();
        boolean isColored = graphSolve.graphColoring(G, color, 0, 4);
        System.out.println(isColored);
    }
}

class GraphSolve {
    public boolean graphColoring(List<Integer>[] G, int[] color, int k, int C) {

        return recurse(k, G, color, C);

    }

    public boolean recurse(int i, List<Integer>[] G, int[] color,
                           int C) {

        if(i == G.length){
            return true;
        }
        for(int j=1; j<=C; j++) {
            if(isSafe(j, G[i], color)){
                color[i] = j;
                if(recurse(i+1, G, color, C)){
                    return true;
                }
                color[i] = 0;
            }
        }
        return false;
    }

    public boolean isSafe(int vertexColor, List<Integer> adj,
                          int[] color) {
        for (int i = 0; i < adj.size(); i++) {
            if (color[adj.get(i)] != 0 && color[adj.get(i)] == vertexColor) {
                return false;
            }
        }
        return true;
    }
}