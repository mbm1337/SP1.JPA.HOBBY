package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class HobbyDAOTest {
    private static HobbyDAO hobbyDAO;
    private static PersonDAO personDAO;
    private static EntityManager em;


    @Mock
    private static HobbyDAO mockHobbyDAO;
    @Mock
    private static Query mockQuery;
    @Mock
    private static EntityManagerFactory mockEntityManagerFactory;
    @Mock
    private static EntityManager mockEntityManager;

    @BeforeAll
    static void setUp() throws NoSuchFieldException, IllegalAccessException {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        em = emf.createEntityManager();
        hobbyDAO = new HobbyDAO();
        personDAO = new PersonDAO();

        mockHobbyDAO = new HobbyDAO();
        mockEntityManagerFactory = mock(EntityManagerFactory.class);
        mockEntityManager = mock(EntityManager.class);
        mockHobbyDAO.emf = mockEntityManagerFactory;
        mockQuery = mock(Query.class);
        when(mockEntityManagerFactory.createEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.createQuery(anyString())).thenReturn(mockQuery);

    }

    @BeforeEach
    void resetDatabase() {
        em.getTransaction().begin();
        Query query = em.createNativeQuery("DELETE FROM hobby_person");
        Query query2 = em.createNativeQuery("DELETE FROM person");
        query.executeUpdate();
        query2.executeUpdate();
        em.getTransaction().commit();

        Hobby hobby1 = hobbyDAO.read(1); //3D-udskrivning
        Hobby hobby2 = hobbyDAO.read(2); //Akrobatik
        Hobby hobby3 = hobbyDAO.read(1); //3D-udskrivning
        Hobby hobby4 = hobbyDAO.read(4); //Amatørradio

        Person person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .address("123 Main St")
                .email("mail@mail.com")
                .gender(Person.Gender.MALE)
                .hobbies(new HashSet<>())
                .build();
        person1.addHobby(hobby1);
        person1.addHobby(hobby2);
        personDAO.create(person1);

        Person person2 = Person.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .address("123 Main St")
                .email("mail@mail.com2")
                .gender(Person.Gender.FEMALE)
                .hobbies(new HashSet<>())
                .build();
        person2.addHobby(hobby3);
        person2.addHobby(hobby4);
        personDAO.create(person2);


    }

    @AfterAll
    public static void tearDown() {
        em.close();
    }


    @Test
    public void countHobbiesPerPersonOnAddress() {
    String address = "123 Main St";
    Map<String, Integer> hobbiesPerPerson = personDAO.countHobbiesPerPersonOnAddress(address);
    assertEquals(2, hobbiesPerPerson.size()); // Assuming there are two persons with hobbies at this address
    assertEquals(Integer.valueOf(2), hobbiesPerPerson.get("John Doe")); // John Doe has 2 hobbies

    }

    @Test
    public void getNumberOfPeopleWithHobby() throws IOException {

        int numberOfPeople = hobbyDAO.getNumberOfPeopleWithHobby(hobbyDAO.read(1));

        assertEquals(2, numberOfPeople);
    }

    @Test
    void testCountPeoplePerHobby() {

        Map<String, Integer> result = hobbyDAO.countPeoplePerHobby();
        assertNotNull(result);
        assertTrue(result.containsKey("3D-udskrivning"));
        assertTrue(result.containsKey("Akrobatik"));
        assertTrue(result.containsKey("Amatørradio"));
        assertEquals(2, result.get("3D-udskrivning"));
        assertEquals(1, result.get("Akrobatik"));
        assertEquals(1, result.get("Amatørradio"));
    }

    @Test
    public void testAllPersonWithAGivenHobby() {
        Person person1 = new Person();
        person1.setFirstName("John");

        Person person2 = new Person();
        person2.setFirstName("Emily");
        String hobbyName = "Cooking";
        List<Person> expectedPersons = new ArrayList<>();
        expectedPersons.add(person1);
        expectedPersons.add(person2);

        when(mockQuery.getResultList()).thenReturn(expectedPersons);

        List<Person> actualPersons = mockHobbyDAO.allPersonWithAGivenHobby(hobbyName);

        Assert.assertEquals(expectedPersons, actualPersons);
    }






}