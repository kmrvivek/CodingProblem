package com.java.problems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.model.Employee;
import com.java.model.SearchInput;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class JsonCustom {

    private static final String link = "https://raw.githubusercontent.com/arcjsonapi/ApiSampleData/master/api/users";
    public static void main(String[] args) throws IOException {
        SearchInput searchInput = new SearchInput();
        searchInput.setAction(SearchInput.ACTION.EQUAL);
        searchInput.setKeyword(SearchInput.KeyWord.CITY);
        String[] city = { "Pune", "Surat" };
        searchInput.setInput(Arrays.asList(city));
        jsonToClassCount(searchInput);

        JsonRead jsonRead = new JsonRead();
        JsonRead.count = 0;
        jsonRead.readJsonpath();

        NestedJson nestedJson = new NestedJson();
        nestedJson.printJson();
    }

    private static void jsonToClassCount(SearchInput searchInput) throws MalformedURLException {
        URL url = new URL(link);

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            InputStream responseStream = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Employee[] emp = mapper.readValue(responseStream, Employee[].class);
            List<String> fields = new ArrayList<>();
            if (searchInput.getKeyword().equals(SearchInput.KeyWord.CITY)) {
                fields = getCities(emp);
            }
            int count =0 ;
            if (SearchInput.ACTION.EQUAL.equals(searchInput.getAction()) || SearchInput.ACTION.IN.equals(searchInput.getAction())) {
                Set<String> cities = new HashSet<>(searchInput.getInput());
                for(String s: fields) {
                    if(cities.stream().anyMatch(s::equalsIgnoreCase)) {
                        count++;
                    }
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getCities(Employee[] emp) {
        List<String> cities = new ArrayList<>();
        for (Employee e : emp) {
            cities.add(e.getAddress().getCity());
        }
        return cities;
    }
}
