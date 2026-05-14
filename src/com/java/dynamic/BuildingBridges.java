package com.java.dynamic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildingBridges {
    // Select max bridges without crossOver
    // Sort the pair based on 1st value if same then choose 2nd value
    // Find LIS of 2nd value

    public static void main(String[] args) {
        List<Map.Entry<Integer, Integer>> bridges1 = new ArrayList<>();
        bridges1.add(new AbstractMap.SimpleEntry<>(6, 2));  bridges1.add(new AbstractMap.SimpleEntry<>(4, 3));
        bridges1.add(new AbstractMap.SimpleEntry<>(2, 6));  bridges1.add(new AbstractMap.SimpleEntry<>(1, 5));

        List<Map.Entry<Integer, Integer>> bridges2 = new ArrayList<>();
        bridges2.add(new AbstractMap.SimpleEntry<>(8, 1)); bridges2.add(new AbstractMap.SimpleEntry<>(1, 2));
        bridges2.add(new AbstractMap.SimpleEntry<>(4, 3)); bridges2.add(new AbstractMap.SimpleEntry<>(3, 4));
        bridges2.add(new AbstractMap.SimpleEntry<>(2, 6)); bridges2.add(new AbstractMap.SimpleEntry<>(6, 7));
        bridges2.add(new AbstractMap.SimpleEntry<>(7, 8)); bridges2.add(new AbstractMap.SimpleEntry<>(5, 5));

        List<Map.Entry<Integer, Integer>> bridges3 = new ArrayList<>();
        bridges3.add(new AbstractMap.SimpleEntry<>(6, 2));  bridges3.add(new AbstractMap.SimpleEntry<>(4, 3));
        bridges3.add(new AbstractMap.SimpleEntry<>(2, 6));  bridges3.add(new AbstractMap.SimpleEntry<>(1, 5));
        bridges3.add(new AbstractMap.SimpleEntry<>(1, 3));

        System.out.println(getBridges(bridges1));
        System.out.println(getBridges(bridges2));
       System.out.println(getBridges(bridges3));
    }


    private  static List<Map.Entry<Integer, Integer>> getBridges(List<Map.Entry<Integer, Integer>> bridges){
        bridges.sort((o1, o2) -> {
            if (o1.getKey() > o2.getKey()) {
                return 1;
            } else if (o1.getKey() < o2.getKey()) {
                return -1;
            } else {
                return o1.getValue() - o2.getValue();
            }
        });

        int[] lis = new int[bridges.size()];
        List<List<Map.Entry<Integer, Integer>>> res = new ArrayList<>();
        List<Map.Entry<Integer, Integer>> p = new ArrayList<>();
        p.add(bridges.get(0));
        res.add(p);
        lis[0] = 1;
        for(int i=1; i<bridges.size(); i++){
            lis[i] = 1;
            List<Map.Entry<Integer, Integer>> q = new ArrayList<>();
            q.add(bridges.get(i));
            res.add(q);
            for(int j=0; j<i; j++){
                if(bridges.get(j).getValue() < bridges.get(i).getValue()){
                    if(lis[j]+1 > lis[i]) {
                        lis[i] = lis[j] + 1;
                        q.clear();
                        q.addAll(res.get(j));
                        q.add(bridges.get(i));
                    }

                }
            }
        }
        int num = 0;
        int size = res.get(0).size();
        for(int i=1; i< res.size(); i++){
            if(res.get(i).size() > size){
                num = i;
                size = res.get(i).size();
            }
        }
        return res.get(num);
    }

}
