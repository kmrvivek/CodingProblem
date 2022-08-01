package com.machinecoding.splitwise.model;

import lombok.Data;

@Data
public class Bill {

    public int billId;
    private int amount;
    private String billInformation;

}
