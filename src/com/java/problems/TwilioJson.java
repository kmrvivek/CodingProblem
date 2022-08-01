package com.java.problems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TwilioJson {

    private static final String link = "https://jsonmock.hackerrank.com/api/transactions/search?userId=";


    public static void main(String[] args) throws IOException {

        readJsonData(1, "debit", "02-2019");
        readJsonData(1, "credit", "02-2019");
        readJsonData(2, "credit", "03-2019");
    }

    private static void readJsonData(int uid, String txnType, String monthYear) throws MalformedURLException {
        URL url = new URL(link+uid);
        List<String> ls = new ArrayList<>();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            InputStream responseStream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder sb = new StringBuilder();
            String input;
            while((input = br.readLine()) != null) {
                sb.append(input);
            }

            JSONObject initialData = new JSONObject(sb.toString().trim());
            int pageNo = (int) initialData.get("total_pages");
            for(int i=0; i<pageNo; i++) {
                url = new URL(link+uid+"&page="+i);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("accept", "application/json");
                responseStream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(responseStream));
                sb = new StringBuilder();
                String jsonInput;
                while((jsonInput = br.readLine()) != null) {
                    sb.append(jsonInput);
                }
                initialData = new JSONObject(sb.toString().trim());
                JSONArray jsonArray = initialData.getJSONArray("data");

                for(int j=0; j<jsonArray.length(); j++) {
                    JSONObject dataJson = jsonArray.getJSONObject(i);
                    long timestamp = dataJson.getLong("timestamp");
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(timestamp);
                    Integer month = cal.get(Calendar.MONTH)+1;
                    Integer year = cal.get(Calendar.YEAR);
                    Integer inputMonth = Integer.valueOf(monthYear.split("-")[0]);
                    Integer inputYear = Integer.valueOf(monthYear.split("-")[1]);
                    String transactionsType = (String) dataJson.get("txnType");
                    if(month.equals(inputMonth) && year.equals(inputYear) &&
                            txnType.equalsIgnoreCase(transactionsType)) {
                        ls.add((String) dataJson.get("amount"));
                    }
                }

            }
            System.out.println(ls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
