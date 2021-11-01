package ru.job4j.tracker;

import ru.job4j.tracker.entity.Item;

import java.util.List;

/**
 * Наша прослойка (между чем и чем?) для SqlTracker
 */
public interface Store extends AutoCloseable {
    void init();

    Item add(Item item);

    boolean replace(Integer id, Item item);

    boolean delete(Integer id);

    List<Item> findAll();

    List<Item> findByName(String key);

    Item findById(Integer id);
}