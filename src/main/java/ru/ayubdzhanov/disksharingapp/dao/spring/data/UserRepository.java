package ru.ayubdzhanov.disksharingapp.dao.spring.data;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
}
