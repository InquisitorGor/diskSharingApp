package ru.ayubdzhanov.disksharingapp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        TypedQuery<User> list = em
                .createQuery("SELECT u FROM User u " +
                                "WHERE u.id = :id ",
                        User.class)
                .setParameter("id", id);
        try {
            return list.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
