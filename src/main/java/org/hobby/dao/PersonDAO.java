package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hobby.model.Person;

import java.util.List;

public class PersonDAO {

    private final EntityManager entityManager;
    private static final PersonDAO instance = new PersonDAO();

    private PersonDAO() {
        this.entityManager = HibernateConfig.getEntityManagerFactory();
    }

    public static PersonDAO getInstance() {
        return instance;
    }

    public List<Person> getPersonsByCity(String city) {
        String jpql = "SELECT p FROM Person p WHERE p.address.city = :city";
        TypedQuery<Person> query = entityManager.createQuery(jpql, Person.class);
        query.setParameter("city", city);
        return query.getResultList();
    }
}
