package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;

import java.util.List;

public class DAO <T> {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();


    public List<Object[]> getAllPostcodesAndCities() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT DISTINCT z.zip, z.cityName FROM Zipcode z";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        return query.getResultList();
    }
    public List<Person> getPersonsByCity(String city) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT p FROM Person p WHERE p.address.city = :city";
        TypedQuery<Person> query = em.createQuery(jpql, Person.class);
        query.setParameter("city", city);
        return query.getResultList();
    }

}
