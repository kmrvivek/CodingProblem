package com.java.problems;

import java.util.*;

public class GoldieRootTree {

    public static void main(String[] args) {
        int[][] arr = {{ 1, 2 },{2, 0},{5, 4},{3, 4},{4,0}};
        int[][] arr2 = {{10, 12},{12, 13},{13, 14},{14, 15}};
        Map<Integer, Integer> input = convertMap(arr2);
        GoldieRootTree goldieRootTree = new GoldieRootTree();
        System.out.println(goldieRootTree.maxTreeNode(input));
    }

    private static Map<Integer, Integer> convertMap(int[][] arr) {
        Map<Integer, Integer> input = new HashMap<>();
        for(int[] a : arr){
            input.put(a[0], a[1]);
        }
        return input;
    }

    public int maxTreeNode(Map<Integer, Integer> input){
        Set<Integer> maxTree = createTree(input);
        int root = -1;
        for(Map.Entry<Integer, Integer> data : input.entrySet()){
            if(maxTree.contains(data.getValue())){
                if(root == -1) {
                    root = data.getValue();
                } else if(data.getKey() == root){
                    root = data.getValue();
                }
            }
        }
        return root;
    }

    public Set<Integer> createTree(Map<Integer, Integer> input){
        List<Set<Integer>> treeSet = new ArrayList<>();
        input.entrySet().stream().forEach(data -> {
            int key = data.getKey();
            int value = data.getValue();
            Set<Integer> keyTree = findTheSet(key, treeSet);
            Set<Integer> valueTree = findTheSet(value, treeSet);
            if(keyTree.isEmpty() && valueTree.isEmpty()){
                Set<Integer> mergedValue = new HashSet<>();
                mergedValue.add(key);
                mergedValue.add(value);
                treeSet.add(mergedValue);
            } else if(keyTree.isEmpty() && !valueTree.isEmpty()){
                valueTree.add(key);
            } else if(!keyTree.isEmpty() && valueTree.isEmpty()){
                keyTree.add(value);
            } else {
                keyTree.addAll(valueTree);
                treeSet.remove(valueTree);
            }
        });
        Set<Integer> maxTree = treeSet.get(0);
        for(Set<Integer> tree: treeSet){
            if(tree.size() > maxTree.size()){
                maxTree = tree;
            }
        }
        return maxTree;
    }

    private Set<Integer> findTheSet(int value, List<Set<Integer>> treeSet) {
       for(Set<Integer> tree: treeSet){
           if(tree.contains(value)){
               return tree;
           }
       }
       return new HashSet<>();
    }
}
