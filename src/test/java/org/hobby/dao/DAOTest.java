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
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public static void tearDown() {
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

    // just need to populate the database with hobby data, person data and hobby_person data
    @Test
    public void countHobbiesPerPersonOnAddress() {
        String address = "123 Main St";
        Map<String, Integer> hobbiesPerPerson = personDAO.countHobbiesPerPersonOnAddress(address);
        assertEquals(2, hobbiesPerPerson.size()); // Assuming there are two persons with hobbies at this address
        assertEquals(Integer.valueOf(2), hobbiesPerPerson.get("John Doe")); // John Doe has 2 hobbies
    }

}

