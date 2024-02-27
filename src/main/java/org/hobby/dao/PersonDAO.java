package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;

public class PersonDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();



    public void getAllPhoneNumbers(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String phoneNumber = em.createQuery("SELECT p.phone FROM Person p WHERE p.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
        System.out.println("Phone number: " + phoneNumber);
    }




}
