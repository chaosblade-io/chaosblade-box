package com.alibaba.chaosblade.box;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.alibaba.chaosblade.box"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}