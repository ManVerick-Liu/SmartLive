package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.rootTable.Home;
import com.MacrohardStudio.model.dro.HomeDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;

import java.util.List;

public interface IHomeService
{
    public ResponseData<Home> add(HomeDro homeDro);
    public ResponseCode modify(Home home);
    public ResponseData<List<Home>> search(Integer home_id, String home_name, String user_id, Integer room_id);
}
