package com.machinecoding.splitwise_tradition.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentSplit extends Split {

    private double percent;

    public PercentSplit(User user, double percent) {
      super(user);
      this.percent = percent;
    }

}
