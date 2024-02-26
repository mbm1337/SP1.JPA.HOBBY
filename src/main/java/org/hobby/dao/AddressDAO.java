package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;

import java.util.List;

public class AddressDAO {

    private final EntityManager entityManager;
    private static final AddressDAO instance = new AddressDAO();

    private AddressDAO() {
        this.entityManager = HibernateConfig.getEntityManagerFactory().createEntityManager();
    }

    public static AddressDAO getInstance() {
        return instance;
    }

    public List<Object[]> getAllPostcodesAndCities() {
        String jpql = "SELECT DISTINCT z.zip, z.cityName FROM Zipcode z";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        return query.getResultList();
    }


}
