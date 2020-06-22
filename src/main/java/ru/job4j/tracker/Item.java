package ru.job4j.tracker;

/**
 Класс модель данных - заявка

 @author RinZ26 */
public class Item {
    /**
     id
     */
    private String id;
    /**
     Название
     */
    private String name;

    public Item(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id = '" + id + '\'' + ", name = '" + name + '\'';
    }
}