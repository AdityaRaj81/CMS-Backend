package com.legalcms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LegalCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegalCmsApplication.class, args);
    }
}
