package com.MacrohardStudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartLiveApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SmartLiveApplication.class, args);
    }

}
