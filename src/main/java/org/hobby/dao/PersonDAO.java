package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;

public class PersonDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();



    public Person save(Person person){

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();
        em.close();
        return person;
    }
    public  void getAllInfo(int personID) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person j = em.createQuery("SELECT p FROM Person p WHERE p.id = :personId", Person.class)
                .setParameter("personId", personID)
                .getSingleResult();
        System.out.println(j);
    }



}
