package com.javaparser.model;

public enum RelaType {
    UNKNOWN("^^"),
    DEPENDENCY("-.->"),
    ASSOCIATION("->"),
    AGGREGATION("<>->"),
    COMPOSITION("<o>->"),
    INHERITANCE("-|>"),
    REALIZATION("-.-|>");
    private String symbol;

    RelaType(String symbol) {
        this.symbol = symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
