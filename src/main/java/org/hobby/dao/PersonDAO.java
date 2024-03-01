package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonDAO  implements IDAO<Person, Integer> {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    @Override
    public void create(Person person) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(person)) {
            person = em.merge(person);
        }
        em.persist(person);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Person read(Integer id) {
        EntityManager em = emf.createEntityManager();
        Person foundPerson = em.find(Person.class, id);
        em.close();
        return foundPerson;
    }

    @Override
    public void update(Person t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Person t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
        em.close();
    }


    public List<Person> getPersonsByCity(String zip) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT p FROM Person p WHERE p.ZipCode = :zip";
        TypedQuery<Person> query = em.createQuery(jpql, Person.class);
        query.setParameter("zip", zip);
        return query.getResultList();
    }

    public Person getPersonByPhoneNumber(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT p FROM Person p WHERE p.phone = :phoneNumber";
        TypedQuery<Person> query = em.createQuery(jpql, Person.class);
        query.setParameter("phoneNumber", phoneNumber);
        List<Person> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Integer> countHobbiesPerPersonOnAddress(String address) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT p FROM Person p WHERE p.address = :address", Person.class)
                .setParameter("address", address)
                .getResultStream()
                .collect(Collectors.toMap(
                        person -> person.getFirstName() + " " + person.getLastName(),
                        person -> person.getHobbies().size()
                ));
      
    }


    public String getPhoneNumber(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Query query = em.createQuery("SELECT p.phone FROM Person p WHERE p.id = :id", String.class);
            query.setParameter("id", id);

            return (String) query.getSingleResult();
        }
    }

    public  Person getAllInfo(int personID) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = :personId", Person.class)
                .setParameter("personId", personID)
                .getSingleResult();
        return  person;
    }



    public Person getPersonByEmailAddress(String email) {
        EntityManager em = emf.createEntityManager();
        Person person = em.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class)
                .setParameter("email", email)
                .getSingleResult();
        return person;

    }

}


