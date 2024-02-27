package org.hobby;

import jakarta.persistence.EntityManager;
import org.hobby.config.HibernateConfig;
import org.hobby.dao.PersonDAO;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = HibernateConfig.buildEntityFactoryConfig().createEntityManager();
        entityManager.getTransaction().begin();

    }
}