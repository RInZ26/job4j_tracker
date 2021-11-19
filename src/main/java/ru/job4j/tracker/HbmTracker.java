package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.entity.Item;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {

    private static HbmTracker INSTANCE = new HbmTracker();

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public static Store getInstance() {
        return INSTANCE;
    }

    private HbmTracker() {
    }

    @Override
    public void init() {
    }

    @Override
    public Item add(Item item) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
        return item;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        boolean result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            int rowsWereAffected = session.createQuery("UPDATE Item SET description = :description,"
                            + " created = :created, " + "name = :name WHERE id = :id ")
                    .setParameter("id", id)
                    .setParameter("description", item.getDescription())
                    .setParameter("created", item.getCreated())
                    .setParameter("name", item.getName())
                    .executeUpdate();
            session.getTransaction().commit();

            result = rowsWereAffected > 0;
        }
        return result;
    }

    /**
     * Здесь и в replace запрос был изменено на HSQL, чтобы ловить количество проапдейченных строк.
     * Тем самым становится понятно, выполнился запрос или нет
     */
    @Override
    public boolean delete(Integer id) {
        boolean result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            int rowsWereAffected = session.createQuery("DELETE from Item WHERE id = :id")
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();

            result = rowsWereAffected > 0;
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Item").list();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Item "
                    + "where name = :name ").setParameter("name", key).list();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public Item findById(Integer id) {
        Item result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.get(Item.class, id);
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public void close()
            throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}