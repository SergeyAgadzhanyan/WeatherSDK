package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Утилитарный класс для работы с Hibernate.
 */
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    /**
     * Получение фабрики сессий Hibernate.
     *
     * @return Фабрика сессий Hibernate.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Создание реестра
                registry = new StandardServiceRegistryBuilder().configure().build();

                // Создание источников метаданных
                MetadataSources sources = new MetadataSources(registry);

                // Создание метаданных
                Metadata metadata = sources.getMetadataBuilder().build();

                // Создание фабрики сессий
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Завершение работы с Hibernate.
     */
    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}




