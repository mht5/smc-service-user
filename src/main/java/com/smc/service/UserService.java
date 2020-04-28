package com.smc.service;

import com.smc.domain.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findUserById(int id);
}
