package com.rounak.impression;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImpressionApplication {

    public static void main(String[] args) {
        EnvConfig.get("DUMMY"); // triggers static block and loads .env
        SpringApplication.run(ImpressionApplication.class, args);
    }

}
