package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {
    private int noteId;
    private String noteTitle;
    private String notedescription;
    private int userId;

    public Note() {}

    public Note(int noteId, String noteTitle, String notedescription, int userId) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.notedescription = notedescription;
        this.userId = userId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
