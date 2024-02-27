package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.hobby.model.PostnummerDTO;
import org.hobby.model.ZipCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.io.IOException;

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
        em.getTransaction().begin();
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
            DAO.PostnummerDAO dao = new DAO.PostnummerDAO();
            try {
                PostnummerDTO postnummer = dao.getPostnummer("1050");
                assertNotNull(postnummer);
                assertEquals("1050", postnummer.getNr());
                assertEquals("København K", postnummer.getNavn());
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
        @Test
        void getPostnummer_WithInvalidPostnummer_ShouldReturnNull() {
            DAO.PostnummerDAO dao = new DAO.PostnummerDAO();
            try {
                PostnummerDTO postnummer = dao.getPostnummer("20030"); // Invalid postnummer
                assertNull(postnummer);
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }





































        @Test
        void testGetPersonByPhoneNumber() {
            // Retrieve a person by their phone number
            Person person = personDAO.getPersonByPhoneNumber("1234567890");

            // Verify that the person object is not null
            assertNotNull(person);

            // Verify that the retrieved person's details are correct
            assertEquals("John", person.getFirstName());
            assertEquals("Doe", person.getLastName());
            assertEquals("1234567890", person.getPhone());
            assertEquals("123 Main St", person.getAddress());
            assertEquals("MALE", person.getGender().toString());
            assertEquals("john.doe@example.com", person.getEmail());
            assertEquals(2000, person.getZipCode().getZip());
        }


}

}

