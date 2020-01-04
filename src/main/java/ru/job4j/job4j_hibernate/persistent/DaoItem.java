package ru.job4j.job4j_hibernate.persistent;

import ru.job4j.job4j_hibernate.models.Item;

import java.util.List;

public interface DaoItem {
    void add(Item item);
    List<Item> getAll();
    List<Item> getAllIsNotDone();
}
