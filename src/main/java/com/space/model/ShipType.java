package com.space.model;

public enum ShipType {
    TRANSPORT("TRANSPORT"),
    MILITARY("MILITARY"),
    MERCHANT("MERCHANT");

    private String type;

    ShipType(String type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return type;
    }
}