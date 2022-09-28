package com.test.springsecurity.services;

import com.test.springsecurity.entity.User;
import com.test.springsecurity.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class TokensServices implements ITokensServices{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    IUserServices userServices;


    @Override
    public String getToken(User user, int mins , boolean withclaims ) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new Exception("Bad credentials!",e);
        }
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getUsername());

        //final String refresh = jwtUtil.generatToken(userDetails, 10);
        return jwtUtil.generatToken(userDetails,mins,withclaims);
    }

    @Override
    public String refreshToken(HttpServletRequest request) throws Exception {
        String authHeader = "";

        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("refresh_token"))
                authHeader = c.getValue();

        }
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer")){
            jwt = authHeader.substring(6);
            username = jwtUtil.extractuUsername(jwt);
        }
        UserDetails userDetails = myUserDetailService.loadUserByUsername(username);

        if (jwtUtil.validateToken(jwt,userDetails)){

            return jwtUtil.generatToken(userDetails,5,true);
        }else {
            return null;
        }

    }
}
