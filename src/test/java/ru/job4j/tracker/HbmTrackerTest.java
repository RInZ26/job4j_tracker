package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Test;
import ru.job4j.tracker.entity.Item;

import java.sql.Timestamp;
import java.time.Instant;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HbmTrackerTest {

    @After
    public void clean() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();

        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE SCHEMA public AND COMMIT NO CHECK").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Test
    public void whenAddItemThenFindIt() {
        Store store = HbmTracker.getInstance();

        Timestamp now = Timestamp.from(Instant.now());
        Item item = Item.of("java", "success", now);
        store.add(item);

        Item actual = store.findById(1);

        Item expected = Item.of("java", "success", now);
        expected.setId(1);

        assertThat(actual, is(expected));
    }

    @Test
    public void whenAddItemsThenFindThem() {
        Store store = HbmTracker.getInstance();

        Timestamp now = Timestamp.from(Instant.now());
        Item java = Item.of("java", "success", now);
        Item spring = Item.of("spring", "money", now);

        store.add(java);
        store.add(spring);

        Item expectedJava = Item.of("java", "success", now);
        expectedJava.setId(1);

        Item expectedSpring = Item.of("spring", "money", now);
        expectedSpring.setId(2);

        Item[] actual = store.findAll().toArray(new Item[0]);
        Item[] expected = new Item[]{expectedJava, expectedSpring};

        assertThat(actual, is(expected));
    }

    @Test
    public void whenReplaceItem() {
        Store store = HbmTracker.getInstance();

        Timestamp now = Timestamp.from(Instant.now());
        Item item = Item.of("java", "success", now);
        store.add(item);

        Item spring = Item.of("spring", "money", now);

        store.replace(item.getId(), spring);

        Item actual = store.findById(1);

        Item expected = Item.of("spring", "money", now);
        expected.setId(1);

        assertThat(actual, is(expected));
        assertThat(store.findAll().size(), is(1));
    }

    @Test
    public void whenDeleteItem() {
        Store store = HbmTracker.getInstance();

        Timestamp now = Timestamp.from(Instant.now());
        Item item = Item.of("java", "success", now);
        store.add(item);

        store.delete(1);

        Item actual = store.findById(1);

        assertNull(actual);
        assertTrue(store.findAll().isEmpty());
    }
}