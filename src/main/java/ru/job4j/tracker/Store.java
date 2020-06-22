package ru.job4j.tracker;

import java.util.List;

/**
 Наша прослойка (между чем и чем?) для SqlTracker
 */
public interface Store extends AutoCloseable {
    void init();

    //TODO поменяли возвращаемый тип Item -> boolean
    boolean add(Item item);

    boolean replace(String id, Item item);

    boolean delete(String id);

    List<Item> findAll();

    List<Item> findByName(String key);

    Item findById(String id);
}