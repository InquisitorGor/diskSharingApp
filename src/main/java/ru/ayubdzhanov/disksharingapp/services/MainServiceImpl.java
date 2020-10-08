package ru.ayubdzhanov.disksharingapp.services;

import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private final Dao dao;

    public MainServiceImpl(Dao dao) {
        this.dao = dao;
    }




}
