package com.java.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchInput {
    KeyWord keyword;
    ACTION action;
    List<String> input;

    public enum KeyWord {
        CITY, ID
    }

    public enum ACTION {
        EQUAL, GREATER, IN, LESS
    }

}
