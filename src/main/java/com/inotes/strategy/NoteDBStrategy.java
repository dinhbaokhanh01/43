package com.inotes.strategy;

import com.inotes.model.Note;
import com.inotes.model.NoteType;
import com.inotes.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Luu tru bang MySQL qua JDBC. */
public class NoteDBStrategy implements NoteStrategy {

    private static final String SELECT =
            "SELECT n.id, n.title, n.content, n.type_id, t.name AS type_name " +
            "FROM notes n LEFT JOIN note_type t ON n.type_id = t.id ";

    /** Kiem tra ket noi, nem NoteException neu khong ket noi duoc. */
    public void testConnection() {
        try (Connection c = DBConnection.getConnection()) {
            // ok
        } catch (SQLException e) {
            throw new NoteException("Khong ket noi duoc MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(Note note) {
        try (Connection c = DBConnection.getConnection()) {
            if (note.getId() == 0) {
                String sql = "INSERT INTO notes(title, content, type_id) VALUES (?,?,?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, note.getTitle());
                    ps.setString(2, note.getContent());
                    ps.setInt(3, note.getTypeId());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) note.setId(rs.getInt(1));
                    }
                }
            } else {
                String sql = "UPDATE notes SET title=?, content=?, type_id=? WHERE id=?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, note.getTitle());
                    ps.setString(2, note.getContent());
                    ps.setInt(3, note.getTypeId());
                    ps.setInt(4, note.getId());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new NoteException("Loi luu ghi chu: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM notes WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new NoteException("Loi xoa ghi chu: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Note> findAll() {
        return query(SELECT + "ORDER BY n.id DESC", null);
    }

    @Override
    public List<Note> search(String keyword) {
        String like = "%" + keyword + "%";
        return query(SELECT + "WHERE n.title LIKE ? OR n.content LIKE ? ORDER BY n.id DESC", like);
    }

    @Override
    public Note findById(int id) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE n.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new NoteException("Loi doc ghi chu: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NoteType> findAllTypes() {
        List<NoteType> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, description FROM note_type ORDER BY id")) {
            while (rs.next()) {
                list.add(new NoteType(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException e) {
            throw new NoteException("Loi doc the loai: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public String getName() { return "MySQL"; }

    private List<Note> query(String sql, String like) {
        List<Note> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (like != null) {
                ps.setString(1, like);
                ps.setString(2, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new NoteException("Loi truy van ghi chu: " + e.getMessage(), e);
        }
        return list;
    }

    private Note map(ResultSet rs) throws SQLException {
        Note n = new Note(rs.getInt("id"), rs.getString("title"),
                rs.getString("content"), rs.getInt("type_id"));
        n.setTypeName(rs.getString("type_name"));
        return n;
    }
}
