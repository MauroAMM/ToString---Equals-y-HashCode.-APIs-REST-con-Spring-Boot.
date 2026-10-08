package com.tp.jpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("tp-jpa-pu");

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void close() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}