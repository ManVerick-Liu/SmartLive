package com.MacrohardStudio.model.enums;

public enum LogTitle
{
    Main("[Main]"),
    MQTT("[MQTT]"),
    JWT("[JWT]"),
    WebSocket("[WebSocket]");

    private final String title;

    LogTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
