package ru.ayubdzhanov.disksharingapp.dao.jpa;

import ru.ayubdzhanov.disksharingapp.domain.Disk;

import java.util.List;

public interface Dao {
    void add(Object obj);

    List<Disk> getAllFreeDisks();

    List<Disk> getAllDisksTakenByUser(Long id);

    List<Disk> getAllDisksWhichWasTaken(Long id);

    Disk findFreeDisk(Long id);
}
