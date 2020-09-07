package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DaoImpl implements Dao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void add(Object obj) {
        em.persist(obj);
    }
}
