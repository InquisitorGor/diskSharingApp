package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.domain.Disk;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DiskDaoImpl implements DiskDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Disk save(Disk disk) {
        if (disk.getId() == null) {
            em.persist(disk);
        } else {
            em.merge(disk);
        }
        return disk;
    }

    @Override
    public List<Disk> getDisks(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d FROM Disk d " +
                                "JOIN TakenItems t ON d.id = t.disk.id " +
                                "WHERE t.originalOwner.id = :id",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    public List<Disk> getFreeDisks() {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "JOIN TakenItems t ON t.disk.id = d.id " +
                                "WHERE t.isFree = true",
                        Disk.class);
        return list.getResultList();
    }

    @Override
    public List<Disk> getDisksTakenByCurrentUser(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "JOIN TakenItems t ON t.disk.id = d.id " +
                                "WHERE t.currentOwner.id = :id",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    public List<Disk> getDisksWhichWasTakenFromUser(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "JOIN TakenItems t ON t.disk.id = d.id " +
                                "WHERE t.originalOwner.id = :id " +
                                "AND t.isFree = false",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    public Disk findFreeDisk(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "JOIN TakenItems t ON d.id = t.disk.id " +
                                "WHERE d.id = :id " +
                                "AND t.isFree = true",
                        Disk.class)
                .setParameter("id", id);
        try {
            return list.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Disk getDisk(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "WHERE d.id = :id ",
                        Disk.class)
                .setParameter("id", id);
        return list.getSingleResult();
    }
}
