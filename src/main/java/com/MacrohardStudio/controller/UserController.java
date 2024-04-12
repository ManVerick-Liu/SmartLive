package com.MacrohardStudio.controller;


import com.MacrohardStudio.annotation.WithJWT;
import com.MacrohardStudio.annotation.WithoutJWT;
import com.MacrohardStudio.model.dto.ResponseDataToken;
import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping(value = "/login")
    public ResponseDataToken<UserDto> login(@RequestBody User user){
        return iUserService.login(user);
    }

    @PostMapping (value = "/register")
    public ResponseCode register(@RequestBody User user){ return iUserService.register(user);}

    @PostMapping(value = "/modify")
    public ResponseCode modify(@RequestBody User user){return iUserService.modify(user);}

    @GetMapping(value = "/search")
    public ResponseData<User> search(@RequestParam(required = false) String user_account){return  iUserService.search(user_account);}

}
