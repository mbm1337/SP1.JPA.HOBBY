package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HobbyDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    public HobbyDAO(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    public HobbyDAO() {
    }

    public Hobby save(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(hobby);
        em.getTransaction().commit();
        em.close();
        return hobby;
    }


    public List<Person> allPersonWithAGivenHobby(String hobbyName) {
        try (EntityManager em = emf.createEntityManager()) {
            Query query = em.createQuery("SELECT h FROM Hobby h " +
                    "JOIN  h.persons p " +
                    "WHERE h.name = :hobbyName");
            query.setParameter("hobbyName", hobbyName);
            return query.getResultList();

        }
    }


}











