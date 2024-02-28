package org.hobby;

import jakarta.persistence.EntityManager;
import org.hobby.config.HibernateConfig;
public class Main {

    public static void main(String[] args) {
        EntityManager entityManager = HibernateConfig.buildEntityFactoryConfig().createEntityManager();
        entityManager.getTransaction().begin();

    }
}