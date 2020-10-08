package ru.ayubdzhanov.disksharingapp.dao;

import ru.ayubdzhanov.disksharingapp.domain.TakenItems;

public interface TakenItemsDao {

    TakenItems save(TakenItems takenItems);

    TakenItems findByDiskId(Long id);
}
