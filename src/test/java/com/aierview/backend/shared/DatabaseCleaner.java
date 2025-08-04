package com.aierview.backend.shared;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clearDatabase() {
        entityManager.createQuery("DELETE FROM AuthJpaEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserJpaEntity").executeUpdate();
    }
}