package com.machinecoding.splitwise.service;

import com.machinecoding.splitwise.model.Flat;
import com.machinecoding.splitwise.model.SplitType;
import com.machinecoding.splitwise.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplitWiseMain {

  public static void main(String[] args) throws IOException {
    Flat flat = new Flat(1, "Sonata");
    BufferedReader br = new BufferedReader(new FileReader("user_data.txt"));
    String input;
    while ((input = br.readLine()) != null){
        String[] userData = input.split(" ");
        flat.getUsers().put(userData[0], new User(userData[0], userData[1], userData[2], userData[3]));
    }
    br.close();
    br = new BufferedReader(new FileReader("splitwise.txt"));
    SplitWise splitWise = new Transaction();
    while ((input = br.readLine()) != null){
        String[] userCommand = input.trim().split(" ");
        if(userCommand.length == 1 && userCommand[0].equalsIgnoreCase("SHOW")){
          splitWise.showExpense(flat);
        } else if(userCommand.length == 2 && userCommand[0].equalsIgnoreCase("SHOW")){
          splitWise.showExpense(flat, userCommand[1]);
        } else {
          if(userCommand[0].equalsIgnoreCase("EXPENSE")){
            String inputUserId = userCommand[1];
            double amount = Double.parseDouble(userCommand[2]);
            int divideShare = Integer.parseInt(userCommand[3]);
            List<String> userIds = new ArrayList<>();
            for(int i=4; i<divideShare+4; i++){
                userIds.add(userCommand[i]);
            }
            SplitType type = SplitType.valueOf(userCommand[divideShare+4].trim());
            List<Double> amountShare = new ArrayList<>();
            if(!type.equals(SplitType.EQUAL)){
              for (int j = divideShare+5; j<userCommand.length; j++){
                amountShare.add(Double.parseDouble(userCommand[j]));
              }
            }
            splitWise.addUpdateExpense(flat, inputUserId, userIds, amount, type, divideShare, amountShare);
          }
        }
    }
    br.close();
  }

}
