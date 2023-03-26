package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NotesMapper notesMapper;

    public void createNote(Note note){
        this.notesMapper.insert(note);
    }

    public void updateNote(Note note){
        this.notesMapper.updateNote(note);
    }

    public void deleteNote(int noteId){
        this.notesMapper.deleteNote(noteId);
    }

    public List<Note> getUserNotes(int userId){
        return this.notesMapper.getNotes(userId);
    }

    public Note getNoteById(int noteId){
        return this.notesMapper.getNoteById(noteId);
    }
}
