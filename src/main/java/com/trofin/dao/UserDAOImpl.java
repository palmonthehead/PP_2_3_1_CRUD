package com.trofin.dao;

import com.trofin.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        try (Session session = em.unwrap(Session.class)) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void saveUser(User user) {
        try (Session session = em.unwrap(Session.class)) {
            session.saveOrUpdate(user);
        }
    }

    @Override
    public User getUserById(int id) {
        try (Session session = em.unwrap(Session.class)) {
            return session.get(User.class, id);
        }
    }

    @Override
    public void deleteUser(int id) {
        Query<User> query;
        try (Session session = em.unwrap(Session.class)) {
            query = session.createQuery("delete from User" +
                    " where id=:userId");
        }
        query.setParameter("userId", id);
        query.executeUpdate();
    }
}
