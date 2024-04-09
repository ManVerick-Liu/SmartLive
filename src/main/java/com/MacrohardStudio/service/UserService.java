package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IUserDao;
import com.MacrohardStudio.model.User;
import com.MacrohardStudio.model.dto.UserDto;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao iUserDao;

    public User search(String user_account)
    {
        User user = new User();
        user.setUser_account(user_account);
        return iUserDao.search(user);
    }

    public UserDto login(User user){

//        User user1 = new User();
//        user1.setUser_account(user.getUser_account());
//        user1.setUser_password(user.getUser_password());

        System.out.println(user.getUser_account()+user.getUser_password());
        UserDto result = iUserDao.login(user);
        //将登录成功的信息转化成jsonUser格式返回给前端
        //JSONObject jsonUser = new JSONObject();

        System.out.println(result.getUser_account());

        System.out.println("login in successfully!");
        return result;
    }


    public ResponseEntity<Integer> register(User user){

        System.out.println("enter register");
        System.out.println(user.getUser_name());
        iUserDao.register(user);

        System.out.println("register successfully !");

        return ResponseEntity.status(200).body(200);
    }

    public ResponseEntity<Integer> modify(User user){

        //User user = new User();
        System.out.println(user.getUser_id());
        User userReg = iUserDao.select(user.getUser_id());
        System.out.println(userReg.getUser_name());

        if(user.getUser_name()!=userReg.getUser_name()&&user.getUser_name()!=null)
        {
            userReg.setUser_name(user.getUser_name());

        }
        if(user.getUser_password()!=userReg.getUser_password()&&user.getUser_password()!=null)
        {
            userReg.setUser_password(user.getUser_password());

        }
        if(user.getUser_phone()!=userReg.getUser_phone()&&user.getUser_phone()!=null)
        {
            userReg.setUser_phone(user.getUser_phone());

        }
        if(user.getUser_email()!=userReg.getUser_email()&&user.getUser_email()!=null)
        {
            userReg.setUser_email(user.getUser_email());

        }
        if(user.getUser_avatar_url()!=userReg.getUser_avatar_url()&&user.getUser_avatar_url()!=null)
        {
            userReg.setUser_avatar_url(user.getUser_avatar_url());

        }


        iUserDao.modify(userReg);
        System.out.println("modify ok!");

        return ResponseEntity.status(200).body(200);


    }
}
