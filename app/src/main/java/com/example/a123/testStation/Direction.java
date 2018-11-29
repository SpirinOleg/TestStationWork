package com.example.a123.testStation;

public enum Direction {
    FROM(0),
    TO(1);

    private final int sqlValue;

    Direction(int sqlValue) {
        this.sqlValue = sqlValue;
    }

    public int getSqlValue() {
        return sqlValue;
    }
}
