package ru.job4j.tracker;

import ru.job4j.tracker.entity.Item;

/**
 * Класс -  Опция показать элементы
 *
 * @author RinZ26
 */
public class FindAllAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
        return "=== Show the all items ===";
    }

    /**
     * Производит заданные операции с tracker
     *
     * @param input
     *         отвечает за ввод данных от пользователя
     * @param tracker
     *         коллекция заявок
     *
     * @return true - пользователь в системе false - вышел
     */
    @Override
    public boolean execute(Input input, Store tracker) {
        for (Item item1 : tracker.findAll()) {
            System.out.println(item1);
        }
        return true;
    }
}
