package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.Home;
import com.MacrohardStudio.model.dro.HomeDro;
import com.MacrohardStudio.service.interfaces.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/home")
public class HomeController
{
    @Autowired
    private IHomeService iHomeService;

    @PostMapping(value = "/add")
    public Home add(@RequestBody HomeDro homeDro)
    {
        return iHomeService.add(homeDro);
    }
    @PostMapping(value = "/modify")
    public ResponseEntity<Integer> modify(@RequestBody Home home)
    {
        return iHomeService.modify(home);
    }
    @GetMapping(value = "/search")
    public List<Home> search
            (@RequestParam(required = false) Integer home_id,
             @RequestParam(required = false) String home_name,
             @RequestParam(required = false) String user_id,
             @RequestParam(required = false) Integer room_id)
    {
        return iHomeService.search(home_id, home_name, user_id, room_id);
    }
}
