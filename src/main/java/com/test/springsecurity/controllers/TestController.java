package com.test.springsecurity.controllers;




import com.test.springsecurity.entity.User;
import com.test.springsecurity.repositories.UserRepository;
import com.test.springsecurity.services.IUserServices;
import com.test.springsecurity.services.MyUserDetailService;
import com.test.springsecurity.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TestController {
    @Autowired
    UserRepository userRepository;

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
    public String user(){
        return ("<h2>welcome user</h2>");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody User user) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new Exception("Bad credentials!",e);
        }
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generatToken(userDetails);
        return ResponseEntity.ok(new String(jwt));
    }

    @PostMapping("/save")
    public User saveUser(@RequestBody User user){
        return userServices.saveUser(user);
    }

}
