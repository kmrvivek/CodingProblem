package com.machinecoding.splitwise_tradition.service;

import com.machinecoding.splitwise_tradition.model.EqualSplit;
import com.machinecoding.splitwise_tradition.model.ExactSplit;
import com.machinecoding.splitwise_tradition.model.ExpenseType;
import com.machinecoding.splitwise_tradition.model.PercentSplit;
import com.machinecoding.splitwise_tradition.model.Split;
import com.machinecoding.splitwise_tradition.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

  public static void main(String[] args) throws IOException {
    ExpenseManager expenseManager = new ExpenseManager();

    expenseManager.addUser(new User("u1", "User1", "gaurav@workat.tech", "9876543210"));
    expenseManager.addUser(new User("u2", "User2", "sagar@workat.tech", "9876543210"));
    expenseManager.addUser(new User("u3", "User3", "hi@workat.tech", "9876543210"));
    expenseManager.addUser(new User("u4", "User4", "mock-interviews@workat.tech", "9876543210"));

    BufferedReader br = new BufferedReader(new FileReader("splitwise.txt"));
    String command;
    while ((command = br.readLine()) != null) {
      String[] commands = command.split(" ");
      String commandType = commands[0];

      switch (commandType) {
        case "SHOW":
          if (commands.length == 1) {
            expenseManager.showBalances();
          } else {
            expenseManager.showBalance(commands[1]);
          }
          break;
        case "EXPENSE":
          String paidBy = commands[1];
          Double amount = Double.parseDouble(commands[2]);
          int noOfUsers = Integer.parseInt(commands[3]);
          String expenseType = commands[4 + noOfUsers];
          List<Split> splits = new ArrayList<>();
          switch (expenseType) {
            case "EQUAL":
              for (int i = 0; i < noOfUsers; i++) {
                splits.add(new EqualSplit(expenseManager.userMap.get(commands[4 + i])));
              }
              expenseManager.addExpense(ExpenseType.EQUAL, amount, paidBy, splits, null);
              break;
            case "EXACT":
              for (int i = 0; i < noOfUsers; i++) {
                splits.add(new ExactSplit(expenseManager.userMap.get(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
              }
              expenseManager.addExpense(ExpenseType.EXACT, amount, paidBy, splits, null);
              break;
            case "PERCENT":
              for (int i = 0; i < noOfUsers; i++) {
                splits.add(new PercentSplit(expenseManager.userMap.get(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
              }
              expenseManager.addExpense(ExpenseType.PERCENT, amount, paidBy, splits, null);
              break;
          }
          break;
      }

    }
    br.close();
  }

}
