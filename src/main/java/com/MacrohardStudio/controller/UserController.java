package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @PostMapping(value = "/login")
    public ResponseData<UserDto> login(@RequestBody User user){return iUserService.login(user);}

    @PostMapping (value = "/register")
    public ResponseCode register(@RequestBody User user){ return iUserService.register(user);}

    @PostMapping(value = "/modify")
    public ResponseCode modify(@RequestBody User user){return iUserService.modify(user);}

    @GetMapping(value = "/search")
    public ResponseData<User> search(@RequestParam String user_account){return  iUserService.search(user_account);}

}
