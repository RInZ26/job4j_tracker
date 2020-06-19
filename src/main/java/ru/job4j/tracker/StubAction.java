package ru.job4j.tracker;

/**
 * Класс - заглушка для тестирования классов UserAction
 */
public class StubAction implements UserAction {
    /**
     * Проверка, выбран ли этот пункт меню
     */
    private boolean call = false;

    /**
     * Возвращает название действия
     *
     * @return ~
     */
    @Override
    public String name() {
        return "Stub action";
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
        call = true;
        return false;
    }

    /**
     * Геттер для call
     * @return ~
     */
    public boolean isCall() {
        return call;
    }
}
