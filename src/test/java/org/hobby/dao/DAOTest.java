package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.hobby.model.ZipDTO;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DAOTest {

    private static HobbyDAO hobbyDAO;
    private static PersonDAO personDAO;
    private static ZipCodeDAO zipCodeDAO;
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
        zipCodeDAO = new ZipCodeDAO();


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
    void testGetPersonByPhoneNumber() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("1234567890")
                .ZipCode(zipCodeDAO.read(2000))
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
        assertEquals(2000, person.getZipCode().getZip());
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


    @Test
    public void countHobbiesPerPersonOnAddress() {
        Hobby hobby1 = hobbyDAO.read(1);
        Hobby hobby2 = hobbyDAO.read(2);
        Hobby hobby3 = hobbyDAO.read(3);
        Hobby hobby4 = hobbyDAO.read(4);

        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .ZipCode(zipCodeDAO.read(2505))
                .address("123 Main St")
                .email("mail@mail.com")
                .gender(Person.Gender.MALE)
                .hobbies(new HashSet<>())
                .build();
        person.setZipCode(zipCodeDAO.read(2505));
        person.addHobby(hobby1);
        person.addHobby(hobby2);
        personDAO.create(person);

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
        person2.setZipCode(zipCodeDAO.read(2505));
        person2.addHobby(hobby3);
        person2.addHobby(hobby4);
        personDAO.create(person2);


        String address = "123 Main St";
        Map<String, Integer> hobbiesPerPerson = personDAO.countHobbiesPerPersonOnAddress(address);
        assertEquals(2, hobbiesPerPerson.size()); // Assuming there are two persons with hobbies at this address
        assertEquals(Integer.valueOf(2), hobbiesPerPerson.get("John Doe")); // John Doe has 2 hobbies
    }

    @Test
    public void getNumberOfPeopleWithHobby() throws IOException {

        Hobby hobby = hobbyDAO.read(23);
        Hobby hobby1 = hobbyDAO.read(23);

        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(new Date(1990, 1, 1))
                .phone("12345678")
                .address("123 Main St")
                .email("mail@mail.com")
                .gender(Person.Gender.MALE)
                .hobbies(new HashSet<>())
                .build();
        person.setZipCode(zipCodeDAO.read(2505));
        person.addHobby(hobby);
        personDAO.create(person);

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

        person2.setZipCode(zipCodeDAO.read(2505));
        person2.addHobby(hobby1);
        personDAO.create(person2);

        int numberOfPeople = hobbyDAO.getNumberOfPeopleWithHobby(hobby);

        assertEquals(2, numberOfPeople);
    }

    @Test
    void testCountPeoplePerHobby() {
        Hobby hobby1 = hobbyDAO.read(279);// Fodbold

        Hobby hobby2 = hobbyDAO.read(438);// Vandpolo
        Hobby hobby3 = hobbyDAO.read(279);// Fodbold


        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setEmail("john@example.com"); // Set the email address
        person1.addHobby(hobby1);

        Person person2 = new Person();
        person2.setFirstName("Emily");
        person2.setEmail("emily@example.com"); // Set the email address
        person2.addHobby(hobby2);
        person2.addHobby(hobby3);

       personDAO.create(person1);
       personDAO.create(person2);


        Map<String, Integer> result = hobbyDAO.countPeoplePerHobby();
        assertNotNull(result);
        assertEquals(2, result.get("Fodbold"));
        assertTrue(result.containsKey("Fodbold"));
        assertTrue(result.containsKey("Vandpolo"));
        assertEquals(2, result.get("Fodbold"));
        assertEquals(1, result.get("Vandpolo"));
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

