package com.machinecoding.splitwise.model;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Flat {

    private final int flatId;
    private Map<String, User> users = new LinkedHashMap<>();
    private String houseName;

    public Flat(int flatId, String houseName) {
        this.flatId = flatId;
        this.houseName = houseName;
    }
}
