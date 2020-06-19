package ru.job4j.tracker;

/**
 * Класс -  Опция создать элемент
 *
 * @author RinZ26
 */
public class CreateAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
	return "=== Create a new item ===";
    }

    /**
     * Производит заданные операции с tracker
     *
     * @param input   отвечает за ввод данных от пользователя
     * @param tracker коллекция заявок
     * @return true - пользователь в системе false - вышел
     */
    @Override
    public boolean execute(Input input, Tracker tracker) {
	String name = input.askStr("Enter the name: ");
	tracker.add(new Item(name));
	return true;
    }
}
