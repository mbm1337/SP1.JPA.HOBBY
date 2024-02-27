package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DAO <T> {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    EntityManager em = emf.createEntityManager();


    public void save(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
        em.close();
    }

    public T findById(int id, Class<T> t) {
        EntityManager em = emf.createEntityManager();
        T foundT = em.find(t, id);
        em.close();
        return foundT;
    }

    public void delete(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
        em.close();
    }

    public void update(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        em.close();
    }

    public int getNumberOfPeopleWithHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        int numberOfPeople = em.createQuery("SELECT p FROM Person p WHERE :hobby MEMBER OF p.hobbies", Hobby.class).setParameter("hobby", hobby).getResultList().size();
        em.getTransaction().commit();
        em.close();
        return numberOfPeople;
    }

    public List<Object[]> getAllPostcodesAndCities() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT DISTINCT z.zip, z.city FROM ZipCode z";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        return query.getResultList();
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
        return em.createQuery(
                        "SELECT p FROM Person p WHERE p.address = :address", Person.class)
                .setParameter("address", address)
                .getResultStream()
                .collect(Collectors.toMap(
                        person -> person.getFirstName() + " " + person.getLastName(),
                        person -> person.getHobbies().size()
                ));
      
    }













































    public Map<String, Integer> countPeoplePerHobby() {
        String jpql = "SELECT h.name, COUNT(p.id) FROM Hobby h LEFT JOIN h.persons p GROUP BY h.name";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        List<Object[]> resultList = query.getResultList();

        Map<String, Integer> peoplePerHobby = new HashMap<>();
        for (Object[] result : resultList) {
            String hobbyName = (String) result[0];
            Long count = (Long) result[1];
            peoplePerHobby.put(hobbyName, count.intValue());
        }
        return peoplePerHobby;
    }

}
