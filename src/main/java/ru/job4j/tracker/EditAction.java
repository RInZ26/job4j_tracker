package ru.job4j.tracker;

import ru.job4j.tracker.entity.Item;

/**
 * Класс -  Опция редактировать элемент
 */
public class EditAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
        return "=== Edit the item by id ===";
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
        Integer id =  input.askInt("Enter the item's id ");
        String newItemName = input.askStr(
                System.lineSeparator() + "Enter new name for this item ");
        System.out.println(System.lineSeparator() + "Operation result is "
                                   + tracker.replace(id,
                                                     new Item(newItemName)));
        return true;
    }
}
