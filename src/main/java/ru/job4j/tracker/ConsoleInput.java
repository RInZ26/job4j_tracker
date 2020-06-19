package ru.job4j.tracker;

import java.util.Scanner;

/**
 * Класс замена Scanner
 *
 * @author RinZ26
 */
public class ConsoleInput implements Input {
    /**
     * Взаимодействие со Scanner
     */
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Аналог Scanner.nextLine()
     *
     * @param question строка перед запросом ввода от пользовалея
     * @return пользовательская строка
     */
    @Override
    public String askStr(String question) {
        System.out.println(question);
        return scanner.nextLine();
    }

    /**
     * Аналог askStr, только с числов
     *
     * @param question строка перед запросом ввода от пользовалея
     * @return пользовательское число / -1 ошибка парсинга
     */
    @Override
    public int askInt(String question) {
        return Integer.parseInt(askStr(question));
    }

    /**
     * Перегруженный метод, для проверки, что мы не вышли за предел доступных значений
     *
     * @param question ~
     * @param max      ~
     * @return ~
     */
    @Override
    public int askInt(String question, int max) {
        int select = askInt(askStr(question));
        if (select < 0 || select >= max) {
            throw new IllegalStateException(String.format("Out of about %s > [0, %s]", select, max));
        }
        return select;
    }

}
