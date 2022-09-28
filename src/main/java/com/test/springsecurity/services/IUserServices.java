package com.test.springsecurity.services;

import com.test.springsecurity.entity.User;
import org.springframework.stereotype.Service;


public interface IUserServices {
    public User encryptPassword(User user);
    public User saveUser(User user);
    public User findByUsername(String username);
}
