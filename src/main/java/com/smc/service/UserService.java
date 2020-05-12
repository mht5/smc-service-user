package com.smc.service;

import com.smc.domain.User;
import com.smc.pojo.LoginResponse;

import java.util.List;

public interface UserService {

    int register(User user) throws Exception;

    boolean confirmRegistration(User user) throws Exception;

    List<User> findAll();

    User findUserById(int id);

    LoginResponse login(User user) throws Exception;

    boolean updateUser(User user) throws Exception;
}
