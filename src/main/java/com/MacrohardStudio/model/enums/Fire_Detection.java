package com.MacrohardStudio.model.enums;

public enum Fire_Detection
{
    fire(1),
    normal(0);

    private int intValue;

    Fire_Detection(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public static Fire_Detection fromInt(int intValue) {
        for (Fire_Detection enumValue : Fire_Detection.values()) {
            if (enumValue.getIntValue() == intValue) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid int value: " + intValue);
    }
}
