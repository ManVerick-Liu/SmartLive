package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IHomeDao;
import com.MacrohardStudio.model.rootTable.Home;
import com.MacrohardStudio.model.followTable.User_Home;
import com.MacrohardStudio.model.dro.HomeDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.service.interfaces.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HomeService implements IHomeService
{
    @Autowired
    private IHomeDao iHomeDao;

    public ResponseData<Home> add(HomeDro homeDro)
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

        ResponseData<Home> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;
    }
    public ResponseCode modify(Home home)
    {
        Home homeTemp = iHomeDao.searchHomeById(home.getHome_id());

        if(home.getHome_name() != null && !Objects.equals(homeTemp.getHome_name(), home.getHome_name()))
        {
            homeTemp.setHome_name(home.getHome_name());
        }
        iHomeDao.modify(home);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());
        return responseCode;
    }
    public ResponseData<List<Home>> search(Integer home_id, String home_name, String user_id, Integer room_id)
    {
        HomeDro homeDro = new HomeDro();
        homeDro.setHome_name(home_name);
        homeDro.setRoom_id(room_id);
        homeDro.setHome_id(home_id);
        homeDro.setUser_id(user_id);

        List<Home> result = iHomeDao.search(homeDro);

        ResponseData<List<Home>> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;
    }
}
