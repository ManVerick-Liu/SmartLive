package com.MacrohardStudio.service.interfaces;

public interface IMqttService
{
    public void publish(String command);
    public void mqttMessageHandler(String data);
}
