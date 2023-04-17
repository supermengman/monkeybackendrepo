package com.nighthawk.spring_portfolio.mvc.monkeyrace;

public enum Problem {
    FRQ_A_2018("FRQ_A_2018"), FRQ_B_2018("FRQ_B_2018");

    private String name;
    private Problem (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}