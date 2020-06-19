package ru.job4j.tracker;

/**
 * Класс для тестирования StartUI
 *
 * @author RinZ26
 */
public class StubInput implements Input {
    private String[] answers;
    private int position = 0;

    public StubInput(String[] answers) {
        this.answers = answers;
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
		return Integer.parseInt(askStr(question));
    }

    @Override
    public String askStr(String question) {
        return answers[position++];
    }

    @Override
    public int askInt(String question) {
        return Integer.parseInt(askStr(question));
    }
}