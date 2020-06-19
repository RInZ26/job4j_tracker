package ru.job4j.tracker;

/**
 *Интерфейс действия пользователя
 *
 * @author RinZ26
 */
public interface UserAction {
    /**
     * Возвращает название действия
     * @return ~
     */
    String name();

    /**
     * Производит заданные операции с tracker
     * @param input   отвечает за ввод данных от пользователя
     * @param tracker коллекция заявок
     * @return true - пользователь в системе false - вышел
     */
    boolean execute(Input input, Tracker tracker);
}
