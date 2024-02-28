package org.hobby.dao;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.hobby.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

public class HobbyDAOTest {

    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;

    private HobbyDAO hobbyDAO;

    @Before
    public void setUp() {
        entityManagerFactory = mock(EntityManagerFactory.class);
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);

        hobbyDAO = new HobbyDAO(entityManagerFactory);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString())).thenReturn(query);
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

        when(query.getResultList()).thenReturn(expectedPersons);


        List<Person> actualPersons = hobbyDAO.allPersonWithAGivenHobby(hobbyName);

        assertEquals(expectedPersons, actualPersons);
    }
}
