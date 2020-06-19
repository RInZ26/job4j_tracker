package ru.job4j.tracker;

/**
 * Класс - опция завершения работы
 */
public class ExitAction implements UserAction {
    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
	return "=== Exit";
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
	return false;
    }
}
