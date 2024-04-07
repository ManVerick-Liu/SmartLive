package com.MacrohardStudio.controller;

import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @GetMapping(value = "/login")
    public JSONObject login(JSONObject jsonObject) throws JSONException {return iUserService.login(jsonObject);}

    @GetMapping(value = "/register")
    public JSONObject register(JSONObject jsonObject)throws JSONException{ return iUserService.register(jsonObject);}

    @GetMapping(value = "/modify")
    public JSONObject modify(JSONObject jsonObject)throws JSONException{return iUserService.modify(jsonObject);}


}
