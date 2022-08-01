package com.machinecoding.splitwise_tradition.model;

import java.util.List;
public class EqualExpense extends Expense {


  public EqualExpense(double amount, User paidBy,
      List<Split> splits, ExpenseMetadata metadata) {
    super(amount, paidBy, splits, metadata);
  }

  @Override
  public boolean validate() {
    for (Split split : getSplits()) {
      if (!(split instanceof EqualSplit)) {
        return false;
      }
    }

    return true;
  }

}
