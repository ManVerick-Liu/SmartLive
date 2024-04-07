package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.jar.JarException;

@Mapper
public interface IUserDao {

    public User login(User user);
    public void register(User user);
    public void modify(User user);

    public User select(String  user_id);

}
