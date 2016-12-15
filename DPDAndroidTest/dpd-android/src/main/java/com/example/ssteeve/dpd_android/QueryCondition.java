package com.example.ssteeve.dpd_android;

/**
 * Created by ssteeve on 11/2/16.
 */
public class QueryCondition {
    public static final Integer GREATER_THAN = 0;
    public static final Integer GREATER_THAN_EQUAL_TO = 1;
    public static final Integer LESS_THAN = 2;
    public static final Integer LESS_THAN_EQUAL_TO = 3;
    public static final Integer NOT_EQUAL = 4;
    public static final Integer EQUAL = 5;
    public static final Integer CONTAINS = 6;
    public static final Integer REGEX = 7;
    public static final Integer OR = 8;
    public static final Integer NONE = 9;
    public static final Integer AND = 10;

    private QueryCondition() { }
}
