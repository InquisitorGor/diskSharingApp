package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayubdzhanov.disksharingapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
