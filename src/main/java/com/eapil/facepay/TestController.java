package com.eapil.facepay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther JianLinWei
 * @date 2020-03-09 16:39
 */
@RestController
public class TestController {
    @Autowired
    private  Service service;

    @GetMapping("/test")
    public String test(){
        return service.getTest();
    }

}
