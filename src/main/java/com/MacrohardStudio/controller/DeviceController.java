package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
