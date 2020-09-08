package ru.ayubdzhanov.disksharingapp.dao.spring.data;

import org.springframework.data.repository.CrudRepository;
import ru.ayubdzhanov.disksharingapp.domain.Disk;

public interface DiskRepository extends CrudRepository<Disk, Long> {
}
