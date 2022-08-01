package com.machinecoding.splitwise.service;

import com.machinecoding.splitwise.model.Flat;
import com.machinecoding.splitwise.model.SplitType;
import java.util.List;

public interface SplitWise {

    void showFlatMates(Flat flat);

    void addUpdateExpense(Flat flat, String inputUserId, List<String> users, double amount,
        SplitType splitType, int shareDivide, List<Double> amountShare);

    void showExpense(Flat flat);

    void showExpense(Flat flat, String userId);
}
