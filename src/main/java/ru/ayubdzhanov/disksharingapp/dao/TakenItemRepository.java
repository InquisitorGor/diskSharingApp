package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;

public interface TakenItemRepository extends JpaRepository<TakenItems, Long> {

}
