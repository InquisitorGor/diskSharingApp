package ru.ayubdzhanov.disksharingapp.dao;

import ru.ayubdzhanov.disksharingapp.domain.Credential;

public interface CredentialDao {
    Credential save(Credential credential);

    Credential findByUsername(String username);
}
