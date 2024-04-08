package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.UserDto;
import org.springframework.http.ResponseEntity;


import java.util.jar.JarException;

public interface IUserService
{

    public UserDto login(User user);
    public ResponseEntity<Integer> register(User user);
    public ResponseEntity<Integer> modify(User user);

}
