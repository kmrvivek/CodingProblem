package com.machinecoding.splitwise.model;

import lombok.Data;

@Data
public class User extends Expense {

    private String userId;
    private String name;
    private String email;
    private String mobileNo;

    public User(String userId){
        this.userId = userId;
    }

    public User(String userId, String name, String email, String mobileNo) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
    }
}
