package com.chem2cs.controller;

import com.chem2cs.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {
    @Autowired
    WendaService wendaService;
    @RequestMapping(path ={"/setting"})
    @ResponseBody
    public String setting(){
        return "Setting is OK "+wendaService.getMessage(1);
    }
}
