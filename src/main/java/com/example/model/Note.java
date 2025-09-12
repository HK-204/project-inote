package com.example.model;

public class Note {
     private int noteId;
     private int userId;
     private String title;
     private String content;
     private String createAt;

    public Note() {
    }

    public Note(int noteId, int userId, String title, String content, String createAt) {
        this.noteId = noteId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getWordCount() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }
}
