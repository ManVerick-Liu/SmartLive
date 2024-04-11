package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/device")
public class DeviceController
{
    @Autowired
    private IDeviceService iDeviceService;

    @PostMapping (value = "/add")
    public ResponseCode add(@RequestBody DeviceDro deviceDro){ return iDeviceService.add(deviceDro);}

    @PostMapping(value = "/delete")
    public ResponseCode delete(@RequestBody Device device){ return iDeviceService.delete(device);}

    @PostMapping(value = "/modify")
    public ResponseCode modify(@RequestBody DeviceDro deviceDro){ return iDeviceService.modify(deviceDro);}

    @GetMapping(value = "/search")
    public ResponseData<List<Device>> search
            (@RequestParam Integer device_id,
             @RequestParam Integer room_id,
             @RequestParam String device_name,
             @RequestParam Device_Category device_category,
             @RequestParam Integer device_activation)
    { return iDeviceService.search(device_id, room_id, device_name, device_category, device_activation);}

    @PostMapping(value = "/control")
    public ResponseCode control(@RequestBody Device_CommandDro device_commandDro) throws JSONException
    {
        return iDeviceService.control(device_commandDro);
    }
}
