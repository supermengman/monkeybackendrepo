package com.nighthawk.spring_portfolio.mvc.monkeyrace;

public enum Problem {
    FRQ_A_2018("FRQ_A_2018"), FRQ_B_2018("FRQ_B_2018"), FRQ_4A_2017("FRQ_4A_2017"), FRQ_4B_2017("FRQ_4B_2017"), FRQ_A_2016("FRQ_A_2016"), FRQ_B_2016("FRQ_B_2016"), Extra_Credit_Arrays("Extra_Credit_Arrays");

    private String name;
    private Problem (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}