package com.machinecoding.splitwise_tradition.service;

import com.machinecoding.splitwise_tradition.model.EqualExpense;
import com.machinecoding.splitwise_tradition.model.ExactExpense;
import com.machinecoding.splitwise_tradition.model.Expense;
import com.machinecoding.splitwise_tradition.model.ExpenseMetadata;
import com.machinecoding.splitwise_tradition.model.ExpenseType;
import com.machinecoding.splitwise_tradition.model.PercentExpense;
import com.machinecoding.splitwise_tradition.model.PercentSplit;
import com.machinecoding.splitwise_tradition.model.Split;
import com.machinecoding.splitwise_tradition.model.User;
import java.util.List;

public class ExpenseService {

  public static Expense createExpense(ExpenseType expenseType, double amount, User paidBy,
      List<Split> splits, ExpenseMetadata expenseMetadata) {
    switch (expenseType) {
      case EXACT:
        return new ExactExpense(amount, paidBy, splits, expenseMetadata);
      case PERCENT:
        for (Split split : splits) {
          PercentSplit percentSplit = (PercentSplit) split;
          split.setAmount((amount * percentSplit.getPercent()) / 100.0);
        }
        return new PercentExpense(amount, paidBy, splits, expenseMetadata);
      case EQUAL:
        int totalSplits = splits.size();
        double splitAmount = ((double) Math.round(amount * 100 / totalSplits)) / 100.0;
        for (Split split : splits) {
          split.setAmount(splitAmount);
        }
        splits.get(0).setAmount(splitAmount + (amount - splitAmount * totalSplits));
        return new EqualExpense(amount, paidBy, splits, expenseMetadata);
      default:
        return null;
    }
  }

}
