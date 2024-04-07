package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IUserDao;
import com.MacrohardStudio.model.User;
import com.MacrohardStudio.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.jar.JarException;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao iUserDao;

    public JSONObject login(JSONObject jsonObject) throws JSONException {

        //将json 对象转化成User类，并拿出账户和密码进行验证登录
        User user = new User();
        user.setUser_account(jsonObject.getString("user_account"));
        user.setUser_password(jsonObject.getString("user_password"));
        User result = iUserDao.login(user);
        //将登录成功的信息转化成jsonUser格式返回给前端
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("user_name", result.getUser_name());
        jsonUser.put("user_account", result.getUser_account());
        jsonUser.put("user_id", result.getUser_id());
        jsonUser.put("user_phone", result.getUser_phone());
        jsonUser.put("user_email", result.getUser_email());
        jsonUser.put("user_avatar_url", result.getUser_avatar_url());

        System.out.println("login in successfully!");
        return jsonUser;
    }


    public JSONObject register(JSONObject jsonObject) throws JSONException {

        //将Json格式的信息转化成实体类user格式,并写入到数据库
        User user = new User();
        user.setUser_name(jsonObject.getString("user_name"));
        user.setUser_password(jsonObject.getString("user_password"));
        user.setUser_account(jsonObject.getString("user_account"));
        user.setUser_phone(jsonObject.getString("user_phone"));
        user.setUser_email(jsonObject.getString("user_email"));
        user.setUser_avatar_url(jsonObject.getString("user_avatar_url"));
        iUserDao.register(user);

        JSONObject result = new JSONObject();
        result.put("code", 200);


        System.out.println("register successfully !");
        return result;

    }

    public JSONObject modify(JSONObject jsonObject) throws JSONException {

        User user = new User();
        user = iUserDao.select( jsonObject.getString("user_id"));
       if(jsonObject.optJSONObject("user_name")!=null)
       {
           user.setUser_name(jsonObject.getString("user_name"));

       }
        if(jsonObject.optJSONObject("user_password")!=null)
        {
            user.setUser_name(jsonObject.getString("user_password"));

        }
        if(jsonObject.optJSONObject("user_phone")!=null)
        {
            user.setUser_name(jsonObject.getString("user_phone"));

        }
        if(jsonObject.optJSONObject("user_email")!=null)
        {
            user.setUser_name(jsonObject.getString("user_email"));

        }
        if(jsonObject.optJSONObject("user_avatar_url")!=null)
        {
            user.setUser_name(jsonObject.getString("user_avatar_url"));

        }

        iUserDao.modify(user);
        JSONObject result = new JSONObject();
        result.put("code", 200);
        System.out.println("modify ok!");

        return result;


    }
}
