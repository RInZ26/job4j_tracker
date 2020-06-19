package ru.job4j.tracker;

/**
 * Класс для обеспечения проверок ConsoleInput
 *
 * @author RinZ26
 */
public class ValidateInput implements Input {
    private final Input input;

    public ValidateInput(Input input) {
        this.input = input;
    }

    /**
     * Аналог Scanner.nextLine()
     *
     * @param question строка перед запросом ввода от пользовалея
     * @return пользовательская строка
     */
    @Override
    public String askStr(String question) {
        return input.askStr(question);
    }

    @Override
    public int askInt(String question) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = input.askInt(question);
                invalid = false;
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter validate data again.");
            }
        } while (invalid);
        return value;
    }

    @Override
    public int askInt(String question, int max) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = input.askInt(question, max);
                invalid = false;
            } catch (IllegalStateException ils) {
                System.out.println(ils.getMessage());
                System.out.println("Please select key from menu.");
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter validate data again.");
            }
        } while (invalid);
        return value;
    }
}
