package com.kiran.wscube_notes_app.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DB_NAME = "notes_db";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            try {
                instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    public abstract NoteDao noteDao();
}
