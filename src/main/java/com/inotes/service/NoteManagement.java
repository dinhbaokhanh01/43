package com.inotes.service;

import com.inotes.model.Note;
import com.inotes.model.NoteType;
import com.inotes.strategy.NoteFileStrategy;
import com.inotes.strategy.NoteStrategy;

import java.util.List;

/**
 * Lop dieu khien nghiep vu (Context cua Strategy Pattern).
 * Co the doi tang luu tru luc runtime bang setStrategy().
 */
public class NoteManagement {

    private static NoteManagement instance;

    private NoteStrategy strategy;

    private NoteManagement(NoteStrategy strategy) {
        this.strategy = strategy;
    }

    public static synchronized NoteManagement getInstance() {
        if (instance == null) {
            instance = new NoteManagement(new NoteFileStrategy()); // mac dinh: File
        }
        return instance;
    }

    public synchronized void setStrategy(NoteStrategy strategy) {
        this.strategy = strategy;
    }

    public synchronized String getStrategyName() {
        return strategy.getName();
    }

    public synchronized List<Note> searchNotes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return strategy.findAll();
        }
        return strategy.search(keyword.trim());
    }

    public synchronized Note addNote(String title, String content, int typeId) {
        Note note = new Note(strategy);
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(typeId);
        note.save();
        return note;
    }

    public synchronized Note updateNote(int id, String title, String content, int typeId) {
        Note note = new Note(strategy);
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(typeId);
        note.save();
        return note;
    }

    public synchronized void deleteNote(int id) {
        new Note(strategy).delete(id);
    }

    public synchronized Note getNote(int id) {
        return strategy.findById(id);
    }

    public synchronized List<NoteType> getTypes() {
        return strategy.findAllTypes();
    }
}
