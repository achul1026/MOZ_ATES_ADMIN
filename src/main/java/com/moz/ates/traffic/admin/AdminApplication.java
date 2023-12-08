package com.moz.ates.traffic.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan({"com.moz.ates.traffic.admin","com.moz.ates.traffic.common"})
@PropertySource(value = "classpath:application.properties",ignoreResourceNotFound = true)
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    } 

}
