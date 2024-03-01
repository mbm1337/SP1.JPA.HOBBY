package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonDAOTest {

    private static PersonDAO personDAO;
    private static EntityManager em;


    @BeforeAll
    static void setUp() throws NoSuchFieldException, IllegalAccessException {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = emf.createEntityManager();
        personDAO = new PersonDAO();



    }

    @BeforeEach
    void resetDatabase() {
        em.getTransaction().begin();
        Query query = em.createNativeQuery("DELETE FROM hobby_person");
        Query query2 = em.createNativeQuery("DELETE FROM person");
        query.executeUpdate();
        query2.executeUpdate();
        em.getTransaction().commit();
    }

    @AfterAll
    public static void tearDown() {
        em.close();
    }




    @Test
    void testGetPersonByPhoneNumber() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("1234567890")
                .address("123 Main St")
                .email("john.doe@example.com")
                .gender(Person.Gender.MALE)
                .build();
        personDAO.create(person);
        // Retrieve a person by their phone number
        Person foundPerson = personDAO.getPersonByPhoneNumber("1234567890");

        // Verify that the person object is not null
        assertNotNull(foundPerson);

        // Verify that the retrieved person's details are correct
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("1234567890", person.getPhone());
        assertEquals("123 Main St", person.getAddress());
        assertEquals("MALE", person.getGender().toString());
        assertEquals("john.doe@example.com", person.getEmail());
    }



    @Test
    void testGetPhoneNumber() {


        Person p = new Person("John", "Doe", new Date(), Person.Gender.MALE, "1234567890", "john.doe@example.com", "123 Main St");
        personDAO.create(p);
        Person foundPerson =  personDAO.getPersonByEmailAddress("john.doe@example.com");
        String phoneNumber = personDAO.getPhoneNumber(foundPerson.getId());

        String expectedPhoneNumber = "1234567890";
        assertEquals(expectedPhoneNumber, phoneNumber);

    }
    @Test
    void  testgetALlInfoPerson(){
        Person person = new Person("John", "Doe", new Date(), Person.Gender.MALE, "1234567890", "john.doe@example.com", "123 Main St");
        personDAO.create(person);
        Person expected = personDAO.getPersonByPhoneNumber("1234567890");

        Person actualperson = personDAO.getAllInfo(expected.getId());
        assertNotNull(expected);
        assertEquals(expected.toString(), actualperson.toString());
    }

}