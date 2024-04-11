package com.MacrohardStudio.service.interfaces;

import org.springframework.boot.configurationprocessor.json.JSONException;

public interface IMqttService
{
    public void publish(String device_mac_address, String command) throws JSONException;
    public void mqttMessageHandler(String data);
}
