package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayubdzhanov.disksharingapp.domain.Credential;

import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Credential findByUsername(String username);
}
