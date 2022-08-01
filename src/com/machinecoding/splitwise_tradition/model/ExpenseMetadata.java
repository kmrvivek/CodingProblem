package com.machinecoding.splitwise_tradition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseMetadata {

  private String name;
  private String imgUrl;
  private String notes;

}
