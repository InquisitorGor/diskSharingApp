package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.domain.Credential;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class CredentialDaoImpl implements CredentialDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Credential save(Credential credential) {
        if (credential.getId() == null) {
            em.persist(credential);
        } else {
            em.merge(credential);
        }
        return credential;
    }

    @Override
    public Credential findByUsername(String username) {
        TypedQuery<Credential> list = em
                .createQuery("SELECT c FROM Credential c " +
                                "JOIN User u ON u.id = c.user.id " +
                                "WHERE c.username = :username ",
                        Credential.class)
                .setParameter("username", username);
        try {
            return list.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
