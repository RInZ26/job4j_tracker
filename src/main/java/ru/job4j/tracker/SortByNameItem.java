package ru.job4j.tracker;

import java.util.Comparator;
/**
 * Класс-компаратор для сравнения в натуральном порядке
 * @author RinZ26
 */
public class SortByNameItem implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
