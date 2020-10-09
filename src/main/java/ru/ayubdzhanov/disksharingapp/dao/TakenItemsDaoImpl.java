package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class TakenItemsDaoImpl implements TakenItemsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public TakenItems save(TakenItems takenItems) {
        if (takenItems.getId() == null) {
            em.persist(takenItems);
        } else {
            em.merge(takenItems);
        }
        return takenItems;
    }

    @Override
    public TakenItems findByDiskId(Long id) {
        TypedQuery<TakenItems> list = em
                .createQuery("SELECT t FROM TakenItems t " +
                                "WHERE t.disk.id = :id ",
                        TakenItems.class)
                .setParameter("id", id);
        try {
            return list.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
