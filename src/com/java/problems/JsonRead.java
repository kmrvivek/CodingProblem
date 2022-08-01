package com.java.problems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JsonRead {
    private static final String link = "https://raw.githubusercontent.com/arcjsonapi/ApiSampleData/master/api/users";
    static int count = 0;

    public JsonRead(){

    }

    public void readJsonpath() throws MalformedURLException {
        URL url = new URL(link);

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
            String[] city = { "Pune", "Surat" };
            Set<String> hs = new HashSet<>(Arrays.asList(city));
            String val = sb.toString();
            JSONArray jsonArray = new JSONArray(val.trim());
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                traverseAndFindValue(json, hs);
            }

            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void traverseAndFindValue(JSONObject jsonObject, Set<String> hs) {
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if(key.equalsIgnoreCase("city")) {
                String val = (String) jsonObject.get(key);
                if(hs.stream().anyMatch(val::equalsIgnoreCase)) {
                    count++;
                }
            }
            if(jsonObject.get(key) instanceof JSONObject) {
                traverseAndFindValue(jsonObject.getJSONObject(key), hs);
            }
        }
    }


}
