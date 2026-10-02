package com.inotes.model;

import com.inotes.strategy.NoteStrategy;

/**
 * Model ghi chu. save()/delete() duoc uy quyen cho NoteStrategy hien tai
 * nen Note khong can biet du lieu dang luu o MySQL hay File.
 */
public class Note {
    private int id;
    private String title;
    private String content;
    private int typeId;
    private String typeName;          // chi de hien thi
    private NoteStrategy strategy;

    public Note() {}

    public Note(NoteStrategy strategy) {
        this.strategy = strategy;
    }

    public Note(int id, String title, String content, int typeId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.typeId = typeId;
    }

    /** Luu moi (id = 0) hoac cap nhat (id > 0). */
    public void save() {
        requireStrategy();
        strategy.save(this);
    }

    /** Xoa ghi chu theo id. */
    public void delete(int id) {
        requireStrategy();
        strategy.delete(id);
    }

    private void requireStrategy() {
        if (strategy == null) {
            throw new IllegalStateException("Note chua duoc gan NoteStrategy");
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public void setStrategy(NoteStrategy strategy) { this.strategy = strategy; }
}
