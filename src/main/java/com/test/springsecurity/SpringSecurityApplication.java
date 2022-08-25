package com.test.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityApplication {




    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

}
