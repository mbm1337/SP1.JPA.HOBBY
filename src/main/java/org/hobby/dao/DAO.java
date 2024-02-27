package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;

public class DAO <T> {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

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


}
