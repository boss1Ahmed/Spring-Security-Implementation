package com.test.springsecurity.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ResourceControlle {
    @CrossOrigin
    @GetMapping("/test1")
    public String test1(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        System.out.println("????");
        return ("<h2>welcome user</h2>");
    }
}
