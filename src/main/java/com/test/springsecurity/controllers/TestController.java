package com.test.springsecurity.controllers;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.test.springsecurity.entity.User;
import com.test.springsecurity.repositories.UserRepository;
import com.test.springsecurity.services.ITokensServices;
import com.test.springsecurity.services.IUserServices;
import com.test.springsecurity.services.MyUserDetailService;
import com.test.springsecurity.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")

@RestController
public class TestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ITokensServices tokensServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private IUserServices userServices;

    @GetMapping("/")
    public String test(){

        Optional<User> user = userRepository.findByUsername("new user");
        return ("<h1> Your pass is : "+user.get().getPassword()+"</h1>");
    }
    @GetMapping("/admin")
    public String adminTest(){

        //Optional<User> user = userRepository.findByUsername("new user");
        //return ("<h1> Your pass is : "+user.get().getPassword()+"</h1>");
        return "ij";
    }

    @GetMapping("/user/test")
    public String user(HttpServletResponse response){
        //response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        System.out.println("????");
        return ("<h2>welcome user</h2>");
    }

    @PostMapping("/authenticate")
    public void createAuthToken(@RequestBody User user , HttpServletResponse response) throws Exception{

        String refresh_token = "Bearer" + tokensServices.getToken(user,1,false);
        final Cookie cookie = new Cookie("refresh_token", refresh_token );
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (System.currentTimeMillis() + 1000L *60*5));
        response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");

        String access_token = tokensServices.getToken(user,2,true);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);

        response.addCookie(cookie);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @PostMapping("/save")
    public User saveUser(@RequestBody User user){
        return userServices.saveUser(user);
    }

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, String> tokens = new HashMap<>();
        Map<String, String> error = new HashMap<>();
        String access_token = tokensServices.refreshToken(request);
        //response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");

        if (access_token!=null){
            tokens.put("access_token",access_token);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }else{
            response.setStatus(FORBIDDEN.value());
            error.put("error_code", "1");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        }

    }
}
