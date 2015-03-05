package com.flyingh.service.impl;

import com.flyingh.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public void login(String username) {
        System.out.printf("%s login%n", username);
    }

    @Override
    public void logout(String username) {
        System.out.printf("%s logout%n", username);
    }
}
