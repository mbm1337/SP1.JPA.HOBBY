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
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

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
        em = emf.createEntityManager();
        hobbyDAO = new DAO<>();
        personDAO = new DAO<>();
        zipCodeDAO = new DAO<>();
        em.getTransaction().begin();
        Query query = em.createNativeQuery("DELETE FROM hobby_person");
        Query query2 = em.createNativeQuery("DELETE FROM person");
        query.executeUpdate();
        query2.executeUpdate();
        em.getTransaction().commit();

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


    @Test
    void getPostnummer_WithInvalidPostnummer_ShouldReturnNull() {

        try {
            ZipDTO postnummer = zipCodeDAO.getZip("2000"); // Invalid postnummer
            assertNull(postnummer);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
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

    @Test
    public void getNumberOfPeopleWithHobby() throws IOException {

        Hobby hobby = hobbyDAO.findById(23, Hobby.class);
        Hobby hobby1 = hobbyDAO.findById(23, Hobby.class);

        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .ZipCode(zipCodeDAO.findById(2505, ZipCode.class))
                .address("123 Main St")
                .email("mail@mail.com")
                .gender(Person.Gender.MALE)
                .hobbies(new HashSet<>())
                .build();
        person.setZipCode(zipCodeDAO.findById(2505, ZipCode.class));
        person.addHobby(hobby);
        personDAO.save(person);

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .address("123 Main St")
                .email("mail2@mail2.com")
                .gender(Person.Gender.FEMALE)
                .hobbies(new HashSet<>())
                .build();

        person2.setZipCode(zipCodeDAO.findById(2505, ZipCode.class));
        person2.addHobby(hobby1);
        personDAO.save(person2);

        int numberOfPeople = personDAO.getNumberOfPeopleWithHobby(hobby);

        assertEquals(2, numberOfPeople); // Assuming there are two persons with the hobby "Soccer"
    }

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

