package ru.job4j.tracker;

/**
 * Определяет работу получения данных от пользователя в консоли
 *
 * @author RinZ26
 */
public interface Input {
    /**
     * Аналог Scanner.nextLine()
     * @param question строка перед запросом ввода от пользовалея
     * @return пользовательская строка
     */
    String askStr(String question);

    /**
     * Аналог askStr, только с числов
     * @param question строка перед запросом ввода от пользовалея
     * @return пользовательское число
     */
    int askInt(String question);

    /**
     * Перегруженный метод, для проверки, что мы не вышли за предел доступных значений
     * @param question ~
     * @param max ~
     * @return ~
     */
    int askInt(String question, int max);
}
