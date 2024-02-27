package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DAOTest {

    private static DAO<Hobby> hobbyDAO;
    private static DAO<Person> personDAO;
    private static DAO<ZipCode> zipCodeDAO;
    private static EntityManager em;

    private static TypedQuery<Object[]> typedQuery;

    private static DAO dao;

    @BeforeAll
    static void setUp() throws NoSuchFieldException, IllegalAccessException {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = mock(EntityManager.class);

        hobbyDAO = new DAO<>();
        personDAO = new DAO<>();
        zipCodeDAO = new DAO<>();

        // Check if dao is null and instantiate it if necessary
        if (dao == null) {
            dao = new DAO();
        }

        // Set the dao.em field directly using reflection
        Field emField = DAO.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(dao, em);

        typedQuery = mock(TypedQuery.class);
    }

    @AfterAll
    public static void tearDown() {
        em.close();
    }
/*
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
    }*/

    // just need to populate the database with hobby data, person data and hobby_person data
    @Test
    public void countHobbiesPerPersonOnAddress() {
        String address = "123 Main St";
        Map<String, Integer> hobbiesPerPerson = personDAO.countHobbiesPerPersonOnAddress(address);
        assertEquals(2, hobbiesPerPerson.size()); // Assuming there are two persons with hobbies at this address
        assertEquals(Integer.valueOf(2), hobbiesPerPerson.get("John Doe")); // John Doe has 2 hobbies
    }

    @Test
    void countPeoplePerHobby() {
        // Define expected result
        Map<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("Reading", 2);
        expectedResult.put("Gardening", 3);

        // Mock EntityManager and TypedQuery behavior
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(getMockResultList());

        // Invoke the method to get the count of people per hobby
        Map<String, Integer> actualResult = dao.countPeoplePerHobby();

        // Verify the results
        assertEquals(expectedResult, actualResult);
    }

    private List<Object[]> getMockResultList() {
        List<Object[]> resultList = new ArrayList<>();
        // Mock data for "Reading" hobby
        resultList.add(new Object[]{"Reading", 2L});
        // Mock data for "Gardening" hobby
        resultList.add(new Object[]{"Gardening", 3L});
        return resultList;
    }

}

