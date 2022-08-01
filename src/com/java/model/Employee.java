package com.java.model;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String website;
    private Company company;

    @Data
    static class GeoLocation {
        private String lat;
        private String lng;
    }

    @Data
    static class Company {
        private String name;
        private String basename;
    }

}
