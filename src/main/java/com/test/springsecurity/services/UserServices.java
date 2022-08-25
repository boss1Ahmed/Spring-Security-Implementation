package com.test.springsecurity.services;

import com.test.springsecurity.entity.User;
import com.test.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements IUserServices{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Override
    public User encryptPassword(User user) {
        String encryptedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPass);
        return user;
    }
    public User saveUser(User user){
        return userRepository.save(encryptPassword(user));
    }
}
