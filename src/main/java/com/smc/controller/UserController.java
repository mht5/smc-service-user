package com.smc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public String hello1(@RequestParam String name) {
        return "Hello, " + name;
    }

}
