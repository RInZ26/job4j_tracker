package ru.job4j.tracker;

import java.util.List;

/**
 * Наша прослойка (между чем и чем?) для SqlTracker
 */
public interface Store extends AutoCloseable {
    void init();

    /*Вернул Item обратно, т.к. после добавления  вбд, где у нас и
    // проставляется id - будет хорошей идеей получить эту заявку с
    // проставленным oid*/
    Item add(Item item);

    boolean replace(Integer id, Item item);

    boolean delete(Integer id);

    List<Item> findAll();

    List<Item> findByName(String key);

    Item findById(Integer id);
}