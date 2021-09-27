package ru.job4j.tracker;

import java.util.List;

/**
 * Класс 0 опция поиска элементов по ключу
 *
 * @author RinZ26
 */
public class FindItemsByKeyAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
        return "=== Find the items by name ===";
    }

    /**
     * Производит заданные операции с tracker
     *
     * @param input   отвечает за ввод данных от пользователя
     * @param tracker коллекция заявок
     * @return true - пользователь в системе false - вышел
     */
    @Override
    public boolean execute(Input input, Store tracker) {
        String name = input.askStr("Enter the item's name ");
        return tracker.findByName(name).size() > 0;
    }
}
