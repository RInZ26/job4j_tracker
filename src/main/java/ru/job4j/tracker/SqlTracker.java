package ru.job4j.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {
    /**
     Логгер
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            SqlTracker.class.getName());

    /**
     Чтобы не плодить строки каждый раз при вызове метода, лучше их все сюда
     (а-ля Хорстман стр 262) вынести
     */
    private static final String DELETE_QUERY =
            "DELETE FROM items AS i WHERE i" + ".id = ?;";
    private static final String ADD_BY_NAME_QUERY =
            "INSERT INTO items(name) " + "VALUES(?);";
    private static final String ADD_QUERY =
            "INSERT INTO items " + "VALUES(?, ?);";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM items AS i" + " WHERE i.id = ?;";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT * FROM items AS" + " i WHERE i.name = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM items AS i;";
    private static final String REPLACE_QUERY =
            "UPDATE items SET name = ? " + "WHERE id = ?;";

    /**
     Наш коннекшен к ДБ
     */
    private Connection cn;

    /**
     Так как мы из БД забираем просто поля, то нам нужно их как-то
     аггрегировать.
     В старой версии item есть только 1 конструктор, поэтому придётся
     использовать конструктор с name, а id уже через сеттер ставить

     @param id
     - поле id Item'a
     @param name
     - ~ name ~

     @return item из бд
     */
    public static Item parseItem(int id, String name) {
        Item item = new Item(name);
        item.setId(String.valueOf(id));
        return item;
    }

    /**
     Инициализация подключения к СУБД
     */
    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader()
                                              .getResourceAsStream(
                                                      "app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(config.getProperty("url"),
                                             config.getProperty("username"),
                                             config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public boolean add(Item item) {
        PreparedStatement statement;
        boolean result = false;
        try {
            if (item.getId() == null) {
                statement = cn.prepareStatement(ADD_BY_NAME_QUERY);
                statement.setString(1, item.getName());
            } else {
                statement = cn.prepareStatement(ADD_QUERY);
                statement.setInt(1, Integer.parseInt(item.getId()));
                statement.setString(2, item.getName());
            }
            result = 0 > statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("add item fell down", e);
        }
        return result;
    }

    @Override
    public boolean replace(String id, Item item) {
        PreparedStatement statement;
        boolean result = false;
        try {
            statement = cn.prepareStatement(REPLACE_QUERY);
            statement.setString(1, item.getName());
            statement.setInt(2, Integer.parseInt(id));
            result = 0 < statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("replace item fell down", e);
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        PreparedStatement statement;
        boolean result = false;
        try {
            statement = cn.prepareStatement(DELETE_QUERY);
            statement.setInt(1, Integer.parseInt(id));
            result = 0 < statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("delete item fell down", e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        PreparedStatement statement;
        List<Item> resultList = new ArrayList<>();
        try {
            statement = cn.prepareStatement(FIND_ALL_QUERY);
            var rs = statement.executeQuery();
            while (rs.next()) {
                resultList.add(
                        parseItem(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            LOG.error("findAll fell down", e);
        }
        return resultList;
    }

    @Override
    public List<Item> findByName(String key) {
        PreparedStatement statement;
        List<Item> resultList = new ArrayList<>();
        try {
            statement = cn.prepareStatement(FIND_BY_NAME_QUERY);
            statement.setString(1, key);
            var rs = statement.executeQuery();
            while (rs.next()) {
                resultList.add(
                        parseItem(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            LOG.error("findByName item fell down", e);
        }
        return resultList;
    }

    @Override
    public Item findById(String id) {
        PreparedStatement statement;
        Item result = null;
        try {
            statement = cn.prepareStatement(FIND_BY_ID_QUERY);
            statement.setInt(1, Integer.parseInt(id));
            var rs = statement.executeQuery();
            if (rs.next()) {
                result = parseItem(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            LOG.error("replace item fell down", e);
        }
        return result;
    }
}