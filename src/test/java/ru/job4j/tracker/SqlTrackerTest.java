package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.connection.ConnectionRollback;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SqlTrackerTest {
    private String nastya = "NastyaCh";
    private String katya = "Katya";

    public Connection init() {
        try (InputStream in = SqlTracker.class.getClassLoader()
                                              .getResourceAsStream(
                                                      "app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(config.getProperty("url"),
                                               config.getProperty("username"),
                                               config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void createItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(
                ConnectionRollback.create(this.init()))) {
            tracker.add(new Item(nastya));
            assertThat(tracker.findByName(nastya).size(), is(1));
        }
    }

    @Test
    public void deleteItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(
                ConnectionRollback.create(this.init()))) {
            tracker.delete(tracker.add(new Item(nastya)).getId());
            assertThat(tracker.findByName(nastya).size(), is(0));
        }
    }

    @Test
    public void replaceItem() throws Exception {
        try (SqlTracker tracker = new SqlTracker(
                ConnectionRollback.create(this.init()))) {
            String id;
            tracker.replace(id = tracker.add(new Item(nastya)).getId(),
                            new Item(katya));
            assertThat(tracker.findByName(nastya).size(), is(0));
            assertThat(tracker.findByName(katya).get(0).getId(), is(id));
        }
    }
}