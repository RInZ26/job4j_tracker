package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.connection.ConnectionRollback;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static ru.job4j.tracker.MemTracker.EMPTY_ITEM;

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

    //mock tests
    @Test
    public void deleteItemMockTest() {
        UserAction deleteAction = new DeleteAction();

        Item item = new Item("willBeDeleted");

        Input mockedInput = mock(Input.class);
        when(mockedInput.askStr(anyString())).thenReturn(item.getId());

        Store store = new MemTracker();
        item = store.add(item);

        assertThat(store.findById(item.getId()), is(item));
        deleteAction.execute(mockedInput, store);
        assertThat(store.findById(item.getId()), is(EMPTY_ITEM));
    }

    @Test
    public void FindByIdActionMockTest() {
        UserAction findAction = new FindItemsByIdAction();
        Store store = new MemTracker();

        Item item = new Item("willBeDeFound");
        item = store.add(item);

        Input mockedInput = mock(Input.class);
        when(mockedInput.askStr(anyString())).thenReturn(item.getId());

        assertTrue(findAction.execute(mockedInput, store));
    }

    @Test
    public void FindByNameActionActionMockTest() {
        UserAction findAction = new FindItemsByKeyAction();
        Store store = new MemTracker();
        Item item = new Item("willBeFound2");

        Input mockedInput = mock(Input.class);
        when(mockedInput.askStr(anyString())).thenReturn(item.getName());

        assertFalse(findAction.execute(mockedInput, store));
        store.add(item);
        assertTrue(findAction.execute(mockedInput, store));
    }
}
