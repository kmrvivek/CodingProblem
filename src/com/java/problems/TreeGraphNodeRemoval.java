package com.java.problems;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TreeGraphNodeRemoval {

    public static void main(String[] args) {
        int[][] edges = {{1,2}, {1,5}, {2, 3}, {2, 4}, {3, 6}};
        int[] weights = {-1,2, 3, 2, 4, 2, 6};
        int N = 6;
        int res[] = getReducedNodeAndHeight(edges, weights, N);
        System.out.println(Arrays.asList(res));
    }

    private static int[] getReducedNodeAndHeight(int[][] edges, int[] weights, int n) {

        Set<Integer> hs = new HashSet<>();
        for(int i=1; i<=n; i++){
            int num1 = edges[i-1][0];
            int num2 = edges[i-1][1];
            if(hs.contains(num1)){
                hs.remove(num1);
            } else {
                hs.add(num1);
            }
            if(hs.contains(num2)){
                hs.remove(num2);
            } else {
                hs.add(num2);
            }
        }
        for(int i=0; i< edges.length; i++){
            if(hs.contains(edges[i][1])){

            }
        }
        return null;
    }


    /*
    private static boolean removeEvenLeafNodes(Node node) {
		if(node == null) return true;
		System.out.println("in : " + node.value);
		if(node.child == null) {
			if(node.weight%2==0) return true;
			return false;
		}
		for(int i=0 ; i<node.child.size() ; i++) {

			boolean flag = removeEvenLeafNodes(node.child.get(i));
			System.out.println("Node out : " + flag);
			if(flag) {
				node.child.remove(i);
				i--;
			}
		}
		System.out.println("Node size : " + node.value + ", " + node.child.size());
		if(node.child.size() == 0 && node.weight%2==0) return true;

		return false;
	}
     */


}
