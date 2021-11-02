package ru.job4j.tracker;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.connection.ConnectionRollback;
import ru.job4j.tracker.entity.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static ru.job4j.tracker.MemTracker.EMPTY_ITEM;

public class SqlTrackerTest {
    private static Connection connection;

    private String nastya = "NastyaCh";
    private String katya = "Katya";

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader()
                .getResourceAsStream("test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        item = tracker.add(item);
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void createItem() throws Exception {
    SqlTracker tracker = new SqlTracker(
               connection);
            tracker.add(new Item(nastya));
            assertThat(tracker.findByName(nastya).size(), is(1));

    }

    @Test
    public void deleteItem() throws Exception {
   SqlTracker tracker = new SqlTracker(
                connection);
            tracker.delete(tracker.add(new Item(nastya)).getId());
            assertThat(tracker.findByName(nastya).size(), is(0));

    }

    @Test
    public void replaceItem() throws Exception {
       SqlTracker tracker = new SqlTracker(connection);
            Integer id;
            tracker.replace(id = tracker.add(new Item(nastya)).getId(),
                    new Item(katya));
            assertThat(tracker.findByName(nastya).size(), is(0));
            assertThat(tracker.findByName(katya).get(0).getId(), is(id));

    }

    @Test
    public void deleteItemMockTest() {
        UserAction deleteAction = new DeleteAction();

        Item item = new Item("willBeDeleted");
        item.setId(0);

        Input mockedInput = mock(Input.class);

        Store store = new MemTracker();
        item = store.add(item);

        when(mockedInput.askInt(anyString())).thenReturn(item.getId());

        assertThat(store.findById(item.getId()), is(item));
        deleteAction.execute(mockedInput, store);
        assertThat(store.findById(item.getId()), is(EMPTY_ITEM));
    }

    @Test
    public void findByIdActionMockTest() {
        UserAction findAction = new FindItemsByIdAction();
        Store store = new MemTracker();

        Item item = new Item("willBeDeFound");
        item.setId(0);
        item = store.add(item);

        Input mockedInput = mock(Input.class);
        when(mockedInput.askInt(anyString())).thenReturn(item.getId());

        assertTrue(findAction.execute(mockedInput, store));
    }

    @Test
    public void findByNameActionActionMockTest() {
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
