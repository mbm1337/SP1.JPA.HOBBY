package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HobbyDAO implements IDAO<Hobby> {
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    @Override
    public void create(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(hobby)) {
            hobby = em.merge(hobby);
        }
        em.persist(hobby);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Hobby read(int id) {
        EntityManager em = emf.createEntityManager();
        Hobby foundHobby = em.find(Hobby.class, id);
        em.close();
        return foundHobby;
    }

    @Override
    public void update(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(hobby);
        em.getTransaction().commit();
        em.close();

    }

    @Override
    public void delete(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(hobby);
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

    public List<Person> allPersonWithAGivenHobby(String hobbyName) {
        try (EntityManager em = emf.createEntityManager()) {
            Query query = em.createQuery("SELECT h FROM Hobby h " +
                    "JOIN  h.persons p " +
                    "WHERE h.name = :hobbyName");
            query.setParameter("hobbyName", hobbyName);
            return query.getResultList();

        }
    }

    public Map<String, Integer> countPeoplePerHobby() {
        EntityManager em = emf.createEntityManager();
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
