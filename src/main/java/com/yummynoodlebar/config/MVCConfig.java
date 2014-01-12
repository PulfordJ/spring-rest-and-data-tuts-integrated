package com.yummynoodlebar.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
        //(basePackages = "com.yummynoodlebar.rest.controller")
@ComponentScan(basePackages = {"com.yummynoodlebar.rest.controller"})
//@ComponentScan(basePackages = {"com.yummynoodlebar.rest.controller",
//        "com.yummynoodlebar.persistence.repository"})
public class MVCConfig {

}
