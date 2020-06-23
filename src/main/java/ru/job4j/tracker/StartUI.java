package ru.job4j.tracker;

import java.util.Arrays;
import java.util.List;

/**
 * Класс - консольное приложенеие для работы с  Tracker
 *
 * @author RinZ26
 */
public class StartUI {

    public static void main(String[] args) {
        Input validateInput = new ValidateInput(new ConsoleInput());
        StartUI testUI = new StartUI();
        Store tracker = new SqlTracker();
        List<UserAction> userActions = Arrays.asList(new CreateAction(),
                                                     new FindAllAction(),
                                                     new EditAction(),
                                                     new DeleteAction(),
                                                     new FindItemsByIdAction(),
                                                     new FindItemsByKeyAction(),
                                                     new ExitAction());
        testUI.init(validateInput, tracker, userActions);
    }

    /**
     * Инициализируем работу UI
     *
     * @param input
     *         отвечает за ввод данных
     * @param tracker
     *         коллекция заявок
     */
    public void init(Input input, Store tracker, List<UserAction> userActions) {
        boolean run = true;
        while (run) {
            this.showMenu(userActions);
            int userDesire = input.askInt("Select: ", userActions.size() - 1);
            run = userActions.get(userDesire).execute(input, tracker);
        }
    }

    /**
     * Вывод меню на экран
     */
    public void showMenu(List<UserAction> userActions) {
        int indexOfMenu = 0;
        System.out.println("Menu:");
        for (UserAction userAction : userActions) {
            System.out.println(indexOfMenu++ + ". " + userAction.name());
        }

    }
}
