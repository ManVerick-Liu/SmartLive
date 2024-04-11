package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;

import java.util.List;


public interface IUserService
{
    public ResponseData<User> search(String user_account);
    public ResponseData<UserDto> login(User user);
    public ResponseCode register(User user);
    public ResponseCode modify(User user);

}
