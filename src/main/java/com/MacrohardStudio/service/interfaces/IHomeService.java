package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.Home;
import com.MacrohardStudio.model.dro.HomeDro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IHomeService
{
    public Home add(HomeDro homeDro);
    public ResponseEntity<Integer> modify(Home home);
    public List<Home> search(Integer home_id, String home_name, String user_id, Integer room_id);
}
