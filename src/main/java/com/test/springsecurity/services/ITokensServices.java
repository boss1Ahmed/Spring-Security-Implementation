package com.test.springsecurity.services;

import com.test.springsecurity.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface ITokensServices {
    public String getToken(User user,int mins, boolean withclaims) throws Exception;
    public String refreshToken(HttpServletRequest request) throws Exception;
}
