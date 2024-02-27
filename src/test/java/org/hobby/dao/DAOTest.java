package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.hobby.model.ZipDTO;
import org.hobby.model.ZipCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.Date;
import java.util.List;

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
   //     Query query = em.createNativeQuery("DELETE FROM person");
        em.getTransaction().begin();
   //     query.executeUpdate();
        em.getTransaction().commit();

    }

    @AfterAll
     static void tearDown() {

        em.close();

    }

    @Nested
    class PostnummerDAOTest {

        @Test
        void getPostnummer_ShouldReturnPostnummerDTO() {

            try {
                ZipDTO postnummer = zipCodeDAO.getZip("2000");
                assertNotNull(postnummer);
                assertEquals("1050", postnummer.getNr());
                assertEquals("København K", postnummer.getNavn());
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
        @Test
        void getPostnummer_WithInvalidPostnummer_ShouldReturnNull() {

            try {
                ZipDTO postnummer = zipCodeDAO.getZip("2000"); // Invalid postnummer
                assertNull(postnummer);
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }





    }

    @Test
    void getPersonsByCity_ShouldReturnPersonsInCity() {
        // Arrange
        em.getTransaction().begin();
        ZipCode zipCode1 = new ZipCode("1000");
        ZipCode zipCode2 = new ZipCode("2000");
        Person person1 = new Person("John", "Doe", new Date(), Person.Gender.MALE, "1234567890", "john@example.com", "Address1");
        Person person2 = new Person("Jane", "Doe", new Date(), Person.Gender.FEMALE, "0987654321", "jane@example.com", "Address2");
        person1.setZipCode(zipCode1);
        person2.setZipCode(zipCode2);
        em.persist(zipCode1);
        em.persist(zipCode2);
        em.persist(person1);
        em.persist(person2);
        em.getTransaction().commit();
        em.close();

        // Act
        List<Person> persons = personDAO.getPersonsByCity(1000);

        // Assert
        assertEquals(1, persons.size());
        assertEquals("John", persons.get(0).getFirstName());
        assertEquals("Doe", persons.get(0).getLastName());
    }


}

