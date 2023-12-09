package com.kiran.wscube_notes_app.Database;


import android.widget.Toast;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao

public interface NoteDao {
    @Query("SELECT * FROM NOTE")
    List<Note> getNotes();

    @Insert
    void addNote(Note note);

    @Query("UPDATE note SET title = :title, desc = :desc WHERE id = :id")
    void update(int id, String title, String desc);

    @Delete
    void deleteNote(Note note);
}
