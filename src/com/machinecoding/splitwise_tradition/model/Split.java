package com.machinecoding.splitwise_tradition.model;

import lombok.Data;

@Data
public abstract class Split {

  private User user;
  double amount;

  public Split(User user){
    this.user = user;
  }

}
