package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.UserDto;
import org.springframework.http.ResponseEntity;



public interface IUserService
{
    public User search(String user_account);
    public UserDto login(User user);
    public ResponseEntity<Integer> register(User user);
    public ResponseEntity<Integer> modify(User user);

}
