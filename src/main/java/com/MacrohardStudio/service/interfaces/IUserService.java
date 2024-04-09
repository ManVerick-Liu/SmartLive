package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;
import org.springframework.http.ResponseEntity;



public interface IUserService
{
    public ResponseData<User> search(String user_account);
    public ResponseData<UserDto> login(User user);
    public ResponseCode register(User user);
    public ResponseCode modify(User user);

}
