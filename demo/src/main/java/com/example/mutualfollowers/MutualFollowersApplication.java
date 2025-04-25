package com.example.mutualfollowers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MutualFollowersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutualFollowersApplication.class, args);
    }
}