package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;

import java.util.Map;
import java.util.stream.Collectors;

public class PersonDAO {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
    EntityManager em = emf.createEntityManager();


    public int getNumberOfPeopleWithHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        int numberOfPeople = em.createQuery("SELECT p FROM Person p WHERE :hobby MEMBER OF p.hobbies", Hobby.class).setParameter("hobby", hobby).getResultList().size();
        em.getTransaction().commit();
        em.close();
        return numberOfPeople;
    }

    public Map<String, Integer> countHobbiesPerPersonOnAddress(String address) {
        return em.createQuery(
                        "SELECT p FROM Person p WHERE p.address = :address", Person.class)
                .setParameter("address", address)
                .getResultStream()
                .collect(Collectors.toMap(
                        person -> person.getFirstName() + " " + person.getLastName(),
                        person -> person.getHobbies().size()
                ));

    }
}
