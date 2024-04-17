package com.MacrohardStudio.model.enums;

public enum HIS_Detection
{
    detected(1),
    undetected(0);

    private int intValue;

    HIS_Detection(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public static HIS_Detection fromInt(int intValue) {
        for (HIS_Detection enumValue : HIS_Detection.values()) {
            if (enumValue.getIntValue() == intValue) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid int value: " + intValue);
    }
}
