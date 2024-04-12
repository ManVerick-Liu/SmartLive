package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.dto.ResponseDataToken;
import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping(value = "/login")
    public ResponseDataToken<UserDto> login(@RequestBody User user)
    {
        log.info("进入登录接口");
        return iUserService.login(user);
    }

    @PostMapping (value = "/register")
    public ResponseCode register(@RequestBody User user){ return iUserService.register(user);}

    @PostMapping(value = "/modify")
    public ResponseCode modify(@RequestBody User user){return iUserService.modify(user);}

    @GetMapping(value = "/search")
    public ResponseData<User> search(@RequestParam(required = false) String user_account)
    {
        log.info("进入查询接口");
        return  iUserService.search(user_account);
    }

}
