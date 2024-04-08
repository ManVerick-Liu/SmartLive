package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @PostMapping(value = "/login")
    public UserDto login(@RequestBody User user){return iUserService.login(user);}

    @PostMapping (value = "/register")
    public ResponseEntity<Integer> register(@RequestBody User user){ return iUserService.register(user);}

    @PostMapping(value = "/modify")
    public ResponseEntity<Integer> modify(@RequestBody User user){return iUserService.modify(user);}



}
