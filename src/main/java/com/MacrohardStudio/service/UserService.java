package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IUserDao;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao iUserDao;

    public ResponseData<User> search(String user_account)
    {
        User user = new User();
        user.setUser_account(user_account);


        User result =  iUserDao.search(user);

        ResponseData<User> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;
    }

    public ResponseData<UserDto> login(User user)
    {

        UserDto result = iUserDao.login(user);

        ResponseData<UserDto> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;
    }


    public ResponseCode register(User user)
    {
        iUserDao.register(user);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());

        return responseCode;
    }

    public ResponseCode modify(User user)
    {
        User userReg = iUserDao.select(user.getUser_id());

        if(!Objects.equals(user.getUser_name(), userReg.getUser_name()) &&user.getUser_name()!=null)
        {
            userReg.setUser_name(user.getUser_name());

        }
        if(!Objects.equals(user.getUser_password(), userReg.getUser_password()) &&user.getUser_password()!=null)
        {
            userReg.setUser_password(user.getUser_password());

        }
        if(!Objects.equals(user.getUser_phone(), userReg.getUser_phone()) &&user.getUser_phone()!=null)
        {
            userReg.setUser_phone(user.getUser_phone());

        }
        if(!Objects.equals(user.getUser_email(), userReg.getUser_email()) &&user.getUser_email()!=null)
        {
            userReg.setUser_email(user.getUser_email());

        }
        if(!Objects.equals(user.getUser_avatar_url(), userReg.getUser_avatar_url()) &&user.getUser_avatar_url()!=null)
        {
            userReg.setUser_avatar_url(user.getUser_avatar_url());

        }

        iUserDao.modify(userReg);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());

        return responseCode;
    }
}
