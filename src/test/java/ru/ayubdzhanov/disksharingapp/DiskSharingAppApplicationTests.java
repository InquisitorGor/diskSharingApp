package ru.ayubdzhanov.disksharingapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ayubdzhanov.disksharingapp.dao.DaoImpl;

@SpringBootTest
class DiskSharingAppApplicationTests {

    @Autowired
    private DaoImpl dao;


    @Test
    void contextLoads() {
    }

}
