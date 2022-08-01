package com.machinecoding.splitwise.model;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Expense {

    private Map<User, Double> expense = new LinkedHashMap<>();
    private Bill bill;
    private double totalAmount;

}
