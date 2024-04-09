package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IHomeDao;
import com.MacrohardStudio.model.Home;
import com.MacrohardStudio.model.User_Home;
import com.MacrohardStudio.model.dro.HomeDro;
import com.MacrohardStudio.service.interfaces.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HomeService implements IHomeService
{
    @Autowired
    private IHomeDao iHomeDao;

    public Home add(HomeDro homeDro)
    {
        Home home = new Home();
        home.setHome_name(homeDro.getHome_name());
        String user_id = homeDro.getUser_id();

        iHomeDao.addHome(home);

        Home result = iHomeDao.searchHomeById(home.getHome_id());

        User_Home user_home = new User_Home();
        user_home.setUser_id(user_id);
        user_home.setHome_id(result.getHome_id());
        iHomeDao.addUser_Home(user_home);

        return result;
    }
    public ResponseEntity<Integer> modify(Home home)
    {
        Home homeTemp = iHomeDao.searchHomeById(home.getHome_id());

        if(home.getHome_name() != null && !Objects.equals(homeTemp.getHome_name(), home.getHome_name()))
        {
            homeTemp.setHome_name(home.getHome_name());
        }
        iHomeDao.modify(home);

        return ResponseEntity.status(200).body(200);
    }
    public List<Home> search(Integer home_id, String home_name, String user_id, Integer room_id)
    {
        /*Home home = new Home();
        home.setHome_name(homeDro.getHome_name());
        home.setHome_id(homeDro.getHome_id());

        Integer home_id = homeDro.getHome_id();

        String home_name = homeDro.getHome_name();

        String user_id = homeDro.getUser_id();

        Integer room_id = homeDro.getRoom_id();*/

        HomeDro homeDro = new HomeDro();
        homeDro.setHome_name(home_name);
        homeDro.setRoom_id(room_id);
        homeDro.setHome_id(home_id);
        homeDro.setUser_id(user_id);

        /*System.out.println(homeDro.getHome_name());
        System.out.println(homeDro.getRoom_id());
        System.out.println(homeDro.getUser_id());
        System.out.println(homeDro.getHome_id());*/

        return iHomeDao.search(homeDro);
    }
}
