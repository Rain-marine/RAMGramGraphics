package repository.utils;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerProvider {

    private static Map<Long, EntityManager> entityManagers = new HashMap<>();

    public static EntityManager getEntityManager() {
        long threadId = Thread.currentThread().getId();
        if (entityManagers.get(threadId) == null) {
            entityManagers.put(threadId, EntityManagerFactoryProvider.getEntityManagerFactory().createEntityManager());
        }
        if (!entityManagers.get(threadId).isOpen()) {
            entityManagers.put(threadId, EntityManagerFactoryProvider.getEntityManagerFactory().createEntityManager());
        }
        return entityManagers.get(threadId);
    }

    public static void close() {
        long threadId = Thread.currentThread().getId();
        if (entityManagers.get(threadId) != null && entityManagers.get(threadId).isOpen()) {
            entityManagers.get(threadId).close();
        }
        entityManagers.remove(entityManagers.get(threadId));
    }
}
