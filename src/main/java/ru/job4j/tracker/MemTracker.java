package ru.job4j.tracker;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс обертка над массива - храналище заявок
 *
 * @author RinZ26
 */
public class MemTracker implements Store {
    /**
     * Анти Null, константа пустой заявки
     */
    public static final Item EMPTY_ITEM = new Item("empty_item");

    static {
        EMPTY_ITEM.setId(0);
    }

    /**
     * Список для хранения заявок
     */
    private List<Item> items = new ArrayList();

    /**
     * Добавление в список
     *
     * @param item добавляемая заявка
     * @return Новый объект Item с проставленным id
     */
    @Override
    public Item add(Item item) {
        Item result = new Item(item.getName());

        result.setId(generateId());
        items.add(result);
        return result;
    }

    /**
     * Метод для создания Item.id на основе текущей даты и ГСЧ
     *
     * @return полученный id
     */
    private Integer generateId() {
        Random rnd = new Random();
        return rnd.nextInt();
    }

    /**
     * Просто возвращаем список, по сути геттер
     *
     * @return список без null
     */
    @Override
    public List<Item> findAll() {
        return items;
    }

    /**
     * Возвращает список, элементы Item.name == key
     *
     * @param key ключ
     * @return отфильттрованный список
     */
    @Override
    public List<Item> findByName(String key) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(key)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Поиск элемента по id
     *
     * @param id id
     * @return элемент есть ? элемент : Tracker.EMPTY_ITEM
     */
    @Override
    public Item findById(Integer id) {
        Item result = EMPTY_ITEM;
        for (Item item : items) {
            if (item.getId().equals(id)) {
                result = item;
                break;
            }
        }
        return result;
    }

    /**
     * Замена ячеек по id
     *
     * @param id   id ячейки, которой меняем
     * @param item заявка, на которую меняем
     * @return id есть ? поменяли true : false
     */
    @Override
    public boolean replace(Integer id, Item item) {
        boolean result = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                item.setId(id);
                items.set(i, item);
                result = true;
            }
        }
        return result;
    }

    /**
     * Удаление элемента
     *
     * @param id id удаляемоего элемента
     */
    @Override
    public boolean delete(Integer id) {
        boolean result = false;
        for (int c = 0; c < items.size(); c++) {
            if (items.get(c).getId().equals(id)) {
                items.remove(c);
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void init() {
    }

    @Override
    public void close() throws Exception {
    }
}
