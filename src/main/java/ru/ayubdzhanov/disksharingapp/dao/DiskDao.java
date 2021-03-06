package ru.ayubdzhanov.disksharingapp.dao;

import ru.ayubdzhanov.disksharingapp.domain.Disk;

import java.util.List;

public interface DiskDao {

    Disk save(Disk disk);

    List<Disk> getDisks(Long id);

    List<Disk> getFreeDisks();

    List<Disk> getDisksTakenByCurrentUser(Long id);

    List<Disk> getDisksWhichWasTakenFromUser(Long id);

    Disk findFreeDisk(Long id);

    Disk getDisk(Long id);
}
