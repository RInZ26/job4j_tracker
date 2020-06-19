package ru.job4j.tracker;

/**
 * Класс - опция поиска элемента по id
 *
 * @author RinZ26
 */
public class FindItemsByIdAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
	return "=== Find the item by id ===";
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
	String id = input.askStr("Enter the item's id ");
	Item item = tracker.findById(id);
	System.out.println(item == Tracker.EMPTY_ITEM ? "The item with this id doesn't exist" : item);
	return true;
    }
}
