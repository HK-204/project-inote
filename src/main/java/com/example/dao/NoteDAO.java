package com.example.dao;

import com.example.model.Note;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    // create
    public boolean insertNote(Note note) {
        String sql = "INSERT INTO Notes (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, note.getUserId());
            ps.setString(2, note.getTitle());
            ps.setString(3, note.getContent());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    note.setNoteId(keys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // read - lấy all note của user
    public List<Note> findAllByUser(int userId) {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT note_id, user_id, title, content, create_at, updated_at FROM Notes WHERE user_id = ? ORDER BY create_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Note n = new Note();
                    n.setNoteId(rs.getInt("note_id"));
                    n.setUserId(rs.getInt("user_id"));
                    n.setTitle(rs.getString("title"));
                    n.setContent(rs.getString("content"));
                    n.setCreateAt(rs.getString("create_at"));
                    list.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // read - lấy 1 note của user
    public Note findById(int noteId) {
        String sql = "SELECT note_id, user_id, title, content, create_at, updated_at FROM Notes WHERE note_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Note n = new Note();
                    n.setNoteId(rs.getInt("note_id"));
                    n.setUserId(rs.getInt("user_id"));
                    n.setTitle(rs.getString("title"));
                    n.setContent(rs.getString("content"));
                    n.setCreateAt(rs.getString("create_at"));
                    return n;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // update - owner dùng
    public boolean updateNote(Note note) {
        String sql = "UPDATE Notes SET title = ?, content = ? WHERE note_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, note.getTitle());
            ps.setString(2, note.getContent());
            ps.setInt(3, note.getNoteId());
            ps.setInt(4, note.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete - owner dùng
    public boolean deleteNote(int noteId, int userId) {
        String sql = "DELETE FROM Notes WHERE note_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noteId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // đếm note của user
    public int countByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM Notes WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // tìm kiếm note
    public List<Note> searchByUserLike(int userId, String keyword) {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT note_id, user_id, title, content, create_at FROM Notes WHERE user_id = ? AND (title LIKE ? OR content LIKE ?) ORDER BY create_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            String like = "%" + keyword + "%";
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Note n = new Note();
                    n.setNoteId(rs.getInt("note_id"));
                    n.setUserId(rs.getInt("user_id"));
                    n.setTitle(rs.getString("title"));
                    n.setContent(rs.getString("content"));
                    n.setCreateAt(rs.getString("create_at"));
                    list.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
