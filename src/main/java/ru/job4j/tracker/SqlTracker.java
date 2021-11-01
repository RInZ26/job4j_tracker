package ru.job4j.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {
    /**
     * Логгер
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            SqlTracker.class.getName());

    /**
     * Чтобы не плодить строки каждый раз при вызове метода, лучше их все сюда
     * (а-ля Хорстман стр 262) вынести
     */
    private static final String DELETE_QUERY =
            "DELETE FROM items AS i WHERE i" + ".id = ?;";
    private static final String ADD_QUERY =
            "INSERT INTO items(name) " + "VALUES(?);";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM items AS i" + " WHERE i.id = ?;";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT * FROM items AS" + " i WHERE i.name = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM items AS i;";
    private static final String REPLACE_QUERY =
            "UPDATE items SET name = ? " + "WHERE id = ?;";

    /**
     * Наш коннекшен к ДБ
     */
    private Connection cn;

    /**
     * "Всё-таки я верю, что TrackerSql это замаскированный SqlTracker"
     * Как я понимаю, мы здесь будем получать Connection через
     * ConnectionRollback, а местный init просто не будем использовать
     *
     * @param connection подготовленный коннекшен
     */
    public SqlTracker(Connection connection) {
        this.cn = connection;
    }

    /**
     * Так как мы из БД забираем просто поля, то нам нужно их как-то
     * аггрегировать.
     * В старой версии item есть только 1 конструктор, поэтому придётся
     * использовать конструктор с name, а id уже через сеттер ставить
     *
     * @param id
     *         - поле id Item'a
     * @param name
     *         - ~ name ~
     *
     * @return item из бд
     */
    public static Item parseItem(int id, String name) {
        Item item = new Item(name);
        item.setId(id);
        return item;
    }

    /**
     * Инициализация подключения к СУБД
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

    /**
     * Добавление заявки в бд с возвращением сгенерированного в бд id
     * посредством getGeneratedKeys
     *
     * @return заявка с именем переданного параметра item и уже новым id из бд
     */
    @Override
    public Item add(Item item) {
        Item result = null;
        try (PreparedStatement st = cn.prepareStatement(ADD_QUERY,
                                                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, item.getName());
            if (0 < st.executeUpdate()) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    result = new Item(item.getName());
                    if (generatedKeys.next()) {
                        result.setId(
                              generatedKeys.getInt("id"));
                    }
                } catch (SQLException sqle) {
                    LOG.error("Generated keys in add fell down", sqle);
                }
            }
        } catch (Exception e) {
            LOG.error("add item fell down", e);
        }
        return result;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        boolean result = false;
        try (PreparedStatement statement = cn.prepareStatement(REPLACE_QUERY)) {
            statement.setString(1, item.getName());
            statement.setInt(2, id);
            result = 0 < statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("replace item fell down", e);
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        boolean result = false;
        try (PreparedStatement statement = cn.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            result = 0 < statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("delete item fell down", e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> resultList = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement(FIND_ALL_QUERY);
             ResultSet rs = statement.executeQuery()) {
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
        List<Item> resultList = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement(
                FIND_BY_NAME_QUERY)) {
            statement.setString(1, key);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    resultList.add(
                            parseItem(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("findByName item fell down", e);
        }
        return resultList;
    }

    @Override
    public Item findById(Integer id) {
        Item result = null;
        try (PreparedStatement statement = cn.prepareStatement(
                FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = parseItem(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("replace item fell down", e);
        }
        return result;
    }
}