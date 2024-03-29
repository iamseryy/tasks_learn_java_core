package ru.task1.repository;

import ru.task1.config.AppConfig;
import ru.task1.model.Note;

import java.util.HashSet;

public interface Notes {
    void add(Note note);
    HashSet<Note> findAll();

    String noteData = new AppConfig().getProperty("notes");
}
