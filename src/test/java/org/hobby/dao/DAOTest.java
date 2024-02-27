package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.hobby.model.ZipCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;


import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    private static DAO<Hobby> hobbyDAO;
    private static DAO<Person> personDAO;
    private static DAO<ZipCode> zipCodeDAO;
    private static EntityManager em;


    @BeforeAll
    static void setUp() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = emf.createEntityManager();
        hobbyDAO = new DAO<>();
        personDAO = new DAO<>();
        zipCodeDAO = new DAO<>();
        Query query = em.createNativeQuery("DELETE FROM person");
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();

    }

    @AfterAll
     static void tearDown() {
        em.close();

    }


}