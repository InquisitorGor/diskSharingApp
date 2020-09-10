package ru.ayubdzhanov.disksharingapp.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DaoImpl implements Dao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void add(Object obj) {
        em.persist(obj);
    }

    @Override
    @Transactional
    public List<Disk> getAllDisks(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "WHERE d.originalOwner.id = :id",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    @Transactional
    public List<Disk> getAllFreeDisks() {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "INNER JOIN TakenItems t ON t.disk.id = d.id " +
                                "WHERE t.isFree = true",
                        Disk.class);
        return list.getResultList();
    }

    @Override
    @Transactional
    public List<Disk> getAllDisksTakenByUser(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "WHERE d.currentUser.id = :id",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    @Transactional
    public List<Disk> getAllDisksWhichWasTaken(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "WHERE d.originalOwner.id = :id " +
                                "AND d.currentUser.id <> d.originalOwner.id",
                        Disk.class)
                .setParameter("id", id);
        return list.getResultList();
    }

    @Override
    @Transactional
    public Disk findFreeDisk(Long id) {
        TypedQuery<Disk> list = em
                .createQuery("SELECT d from Disk d " +
                                "INNER JOIN TakenItems t ON d.id = t.disk.id " +
                                "WHERE d.id = :id " +
                                "AND t.isFree = true",
                        Disk.class)
                .setParameter("id", id);
        return list.getSingleResult();
    }
}
