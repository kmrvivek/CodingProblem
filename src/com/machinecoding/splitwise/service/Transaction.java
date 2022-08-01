package com.machinecoding.splitwise.service;

import com.machinecoding.splitwise.model.Flat;
import com.machinecoding.splitwise.model.SplitType;
import com.machinecoding.splitwise.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Transaction implements SplitWise {


    @Override
    public void showFlatMates(Flat flat) {
        System.out.println(flat.getHouseName());
        flat.getUsers().forEach((userId, user) -> System.out.print(user.getName() + " "));
        System.out.println();
    }

    @Override
    public void addUpdateExpense(Flat flat, String inputUserId, List<String> users, double amount,
        SplitType splitType, int shareDivide, List<Double> amountShare) {
        if(flat.getUsers().containsKey(inputUserId)){
            User inputUser = flat.getUsers().get(inputUserId);
            Map<String, Double> amountShares = getAmountShares(splitType, amountShare, inputUserId, users, amount);
            Map<String, User> usersShares = flat.getUsers();
            amountShares.forEach((id, val) -> {
                User existUser = usersShares.get(id);
                if(existUser.getExpense().containsKey(inputUser)){
                    existUser.getExpense().put(inputUser,  existUser.getExpense().get(inputUser)+val);
                } else {
                    existUser.getExpense().put(inputUser, val);
                }
            });
        } else {
            System.out.println("User doesn't exist. Add User to Flat");
        }

    }

    private Map<String, Double> getAmountShares(SplitType splitType, List<Double> amountShare, String inputUserId, List<String> users,
        double amount) {
        Map<String, Double> userAmount = new HashMap<>();
        int shareDivide = users.size();
        switch (splitType){
            case EQUAL:
                double shares = getRoundAmount(amount/shareDivide);
                double totalDiff = getRoundAmount(amount - (shares * shareDivide));
                double firstShare = getRoundAmount(shares+totalDiff);
                boolean usedFirstShare = false;
                for (int i=0; i<shareDivide; i++){
                    if(users.get(i).equalsIgnoreCase(inputUserId)) continue;
                    if(!usedFirstShare){
                        userAmount.put(users.get(i), firstShare);
                        usedFirstShare = true;
                    } else {
                        userAmount.put(users.get(i), shares);
                    }
                }
                break;

            case EXACT:
                for (int i=0; i<shareDivide; i++){
                    userAmount.put(users.get(i), amountShare.get(i));
                }
                break;

            case PERCENT:
                for (int i=0; i<shareDivide; i++){
                    if(users.get(i).equalsIgnoreCase(inputUserId)) continue;
                    double userPercentAmount = getRoundAmount((amount * amountShare.get(i))/100);
                    userAmount.put(users.get(i), userPercentAmount);
                }
                break;
        }
        return userAmount;
    }

    @Override
    public void showExpense(Flat flat) {
        AtomicBoolean haveExpense = new AtomicBoolean(false);
        flat.getUsers().forEach((userId, user) -> {
            if(!user.getExpense().isEmpty()){
                haveExpense.set(true);
                user.getExpense().forEach((key, val) -> {
                    System.out.println(user.getName() + " owes " + key.getName() + " " + val);
                });
            }
        });
        if(!haveExpense.get())
        System.out.println("No balances");
    }

    @Override
    public void showExpense(Flat flat, String userId) {
        User user = flat.getUsers().get(userId);
        if(user.getExpense().isEmpty()){
            System.out.println("No balances");
        } else {
            user.getExpense().forEach((key, val) -> {
                System.out.println(user.getName() + " owes " + key.getName() + " " + val);
            });
        }
    }

    private double getRoundAmount(double amount){
        return Math.round(amount * 100.0)/100.0;
    }
}
