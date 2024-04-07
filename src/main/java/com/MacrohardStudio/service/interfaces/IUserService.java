package com.MacrohardStudio.service.interfaces;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.jar.JarException;

public interface IUserService
{
    public JSONObject login(JSONObject jsonObject) throws JSONException;
    public JSONObject register(JSONObject jsonObject) throws JSONException;
    public JSONObject modify(JSONObject jsonObject) throws JSONException;

}
