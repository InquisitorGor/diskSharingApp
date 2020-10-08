package ru.ayubdzhanov.disksharingapp.dao;

import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.User;

public interface UserDao {
    User save(User user);

    User findById(Long id);
}
