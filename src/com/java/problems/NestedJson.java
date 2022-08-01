package com.java.problems;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class NestedJson {

    public void printJson() throws IOException {

        FileReader fr = new FileReader("data.json");
        BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        String input;
        while ((input = br.readLine()) != null) {
            System.out.println(input);
            sb.append(input);
        }
        br.close();

        traverse(sb.toString(), "");

    }

    private void traverse(String contents, String input) {
        JSONObject jsonObject = new JSONObject(contents.trim());
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String input1;
            if (input.isEmpty()) {
                input1 = key;
            } else {
                input1 = input + "." + key;
            }
            if (jsonObject.get(key) instanceof JSONObject) {

                traverse(jsonObject.get(key).toString(), input1);
            } else {
                System.out.println(input1 + " = " + jsonObject.get(key));

            }

        }
    }

}
