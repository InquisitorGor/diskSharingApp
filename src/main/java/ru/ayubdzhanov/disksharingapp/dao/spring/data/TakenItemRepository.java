package ru.ayubdzhanov.disksharingapp.dao.spring.data;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;

import java.util.List;

public interface TakenItemRepository extends JpaRepository<TakenItems, Long> {
        TakenItems findByDiskId(Long id);
}
