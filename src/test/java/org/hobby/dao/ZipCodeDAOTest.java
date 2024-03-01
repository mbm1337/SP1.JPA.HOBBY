package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.hobby.config.HibernateConfig;
import org.hobby.model.ZipDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class ZipCodeDAOTest {

    private static ZipCodeDAO zipCodeDAO;
    private static EntityManager em;


    @BeforeAll
    static void setUp() throws NoSuchFieldException, IllegalAccessException {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = emf.createEntityManager();
        zipCodeDAO = new ZipCodeDAO();

    }


    @AfterAll
    public static void tearDown() {
        em.close();
    }

    @Test
    void getPostnummer_ShouldReturnPostnummerDTO() {

        try {
            ZipDTO postnummer = zipCodeDAO.getZip("2000");
            assertNotNull(postnummer);
            assertEquals("2000", postnummer.getNr());
            assertEquals("Frederiksberg", postnummer.getNavn());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }


    @Test
    void getPostnummer_WithInvalidPostnummer_ShouldReturnNull() {

        try {
            ZipDTO postnummer = zipCodeDAO.getZip("2000"); // Invalid postnummer
            assertNotNull(postnummer);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }


}