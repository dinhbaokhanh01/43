package com.inotes.strategy;

import com.inotes.model.Note;
import com.inotes.model.NoteType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Luu tru ra file text, moi dong: id|title|content|typeId
 * Ky tu dac biet duoc escape: "\" -> "\\", "|" -> "\p", xuong dong -> "\n".
 * File mac dinh: ~/inotes-data/notes.txt
 */
public class NoteFileStrategy implements NoteStrategy {

    private static final List<NoteType> TYPES = Arrays.asList(
            new NoteType(1, "Cá nhân", "Ghi chú đời sống cá nhân"),
            new NoteType(2, "Công việc", "Ghi chú liên quan đến công việc"),
            new NoteType(3, "Học tập", "Ghi chú học tập, nghiên cứu"));

    private final Path file;

    public NoteFileStrategy() {
        this(Paths.get(System.getProperty("user.home"), "inotes-data", "notes.txt"));
    }

    public NoteFileStrategy(Path file) {
        this.file = file;
    }

    @Override
    public synchronized void save(Note note) {
        List<Note> all = readAll();
        if (note.getId() == 0) {
            int max = 0;
            for (Note n : all) max = Math.max(max, n.getId());
            note.setId(max + 1);
            all.add(copy(note));
        } else {
            boolean found = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == note.getId()) {
                    all.set(i, copy(note));
                    found = true;
                    break;
                }
            }
            if (!found) all.add(copy(note));
        }
        writeAll(all);
    }

    @Override
    public synchronized void delete(int id) {
        List<Note> all = readAll();
        all.removeIf(n -> n.getId() == id);
        writeAll(all);
    }

    @Override
    public synchronized List<Note> findAll() {
        List<Note> all = readAll();
        all.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        return all;
    }

    @Override
    public synchronized List<Note> search(String keyword) {
        String k = keyword.toLowerCase();
        List<Note> result = new ArrayList<>();
        for (Note n : findAll()) {
            if (safe(n.getTitle()).toLowerCase().contains(k)
                    || safe(n.getContent()).toLowerCase().contains(k)) {
                result.add(n);
            }
        }
        return result;
    }

    @Override
    public synchronized Note findById(int id) {
        for (Note n : readAll()) if (n.getId() == id) return n;
        return null;
    }

    @Override
    public List<NoteType> findAllTypes() { return TYPES; }

    @Override
    public String getName() { return "File"; }

    // ---------- helpers ----------
    private List<Note> readAll() {
        List<Note> list = new ArrayList<>();
        if (!Files.exists(file)) return list;
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 4) continue;
                Note n = new Note(Integer.parseInt(p[0]), unescape(p[1]), unescape(p[2]),
                        Integer.parseInt(p[3]));
                n.setTypeName(typeName(n.getTypeId()));
                list.add(n);
            }
        } catch (IOException | NumberFormatException e) {
            throw new NoteException("Loi doc file " + file + ": " + e.getMessage(), e);
        }
        return list;
    }

    private void writeAll(List<Note> notes) {
        List<String> lines = new ArrayList<>();
        for (Note n : notes) {
            lines.add(n.getId() + "|" + escape(n.getTitle()) + "|" + escape(n.getContent())
                    + "|" + n.getTypeId());
        }
        try {
            if (file.getParent() != null) Files.createDirectories(file.getParent());
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new NoteException("Loi ghi file " + file + ": " + e.getMessage(), e);
        }
    }

    private Note copy(Note n) {
        Note c = new Note(n.getId(), n.getTitle(), n.getContent(), n.getTypeId());
        c.setTypeName(typeName(n.getTypeId()));
        return c;
    }

    private String typeName(int id) {
        for (NoteType t : TYPES) if (t.getId() == id) return t.getName();
        return "";
    }

    private static String safe(String s) { return s == null ? "" : s; }

    static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            switch (ch) {
                case '\\': sb.append("\\\\"); break;
                case '|':  sb.append("\\p"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': break;
                default:   sb.append(ch);
            }
        }
        return sb.toString();
    }

    static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                switch (next) {
                    case 'p':  sb.append('|'); break;
                    case 'n':  sb.append('\n'); break;
                    case '\\': sb.append('\\'); break;
                    default:   sb.append(next);
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
