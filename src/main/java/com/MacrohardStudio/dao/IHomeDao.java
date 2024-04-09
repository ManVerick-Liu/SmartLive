package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.rootTable.Home;
import com.MacrohardStudio.model.followTable.User_Home;
import com.MacrohardStudio.model.dro.HomeDro;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHomeDao
{
    public void addHome(Home home);
    public void addUser_Home(User_Home user_home);
    public Home searchHomeById(Integer home_id);
    public void modify(Home home);
    public List<Home> search(HomeDro homeDro);
}
