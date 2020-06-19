package ru.job4j.tracker;

import java.util.Comparator;

/**
 * Класс-компаратор для сравнения в обратном порядке
 * @author RinZ26
 */
public class ReverseSortByNameItem implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return o2.getName().compareTo(o1.getName());
    }
}
