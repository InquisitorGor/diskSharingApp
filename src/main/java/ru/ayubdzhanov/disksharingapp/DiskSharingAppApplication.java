package ru.ayubdzhanov.disksharingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DiskSharingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiskSharingAppApplication.class, args);
    }

}
