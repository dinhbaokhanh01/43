package com.inotes.strategy;

import com.inotes.model.Note;
import com.inotes.model.NoteType;

import java.util.List;

/** Hop dong chung cho moi tang luu tru (Strategy Pattern). */
public interface NoteStrategy {
    void save(Note note);
    void delete(int id);
    List<Note> findAll();
    List<Note> search(String keyword);

    // Ham bo tro cho giao dien (xem/sua, dropdown phan loai)
    Note findById(int id);
    List<NoteType> findAllTypes();

    /** Ten hien thi cua chien luoc: "MySQL" hoac "File". */
    String getName();
}
