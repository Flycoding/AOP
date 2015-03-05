package com.flyingh.service;

import com.flyingh.annotation.Log;

@Log
public interface UserService {
    void login(String username);

    void logout(String username);
}
