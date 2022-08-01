package com.machinecoding.splitwise_tradition.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

  private String id;
  private String name;
  private String email;
  private String phone;

}
