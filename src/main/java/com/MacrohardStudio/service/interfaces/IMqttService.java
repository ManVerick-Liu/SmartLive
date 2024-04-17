package com.MacrohardStudio.service.interfaces;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface IMqttService
{
    public void publish(String device_mac_address, String command);
    public void mqttMessageHandler(JSONObject data) throws JSONException;
}
