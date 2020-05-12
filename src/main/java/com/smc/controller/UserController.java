package com.smc.controller;

import com.smc.domain.User;
import com.smc.pojo.LoginResponse;
import com.smc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public int register(@RequestBody User user) throws Exception {
        return userService.register(user);
    }

    @PostMapping("/register-confirm")
    public boolean confirmRegistration(@RequestBody User user) throws Exception {
        return userService.confirmRegistration(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) throws Exception {
        return userService.login(user);
    }

    @PostMapping("/update")
    public boolean updateUser(@RequestBody User user) throws Exception {
        return userService.updateUser(user);
    }

    @GetMapping("/find-by-id")
    public User findUserById(@RequestParam int id) {
        return userService.findUserById(id);
    }

    @GetMapping("/list-all")
    public List<User> findAll() {
        return userService.findAll();
    }

}
