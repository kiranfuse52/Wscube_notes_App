package com.kiran.wscube_notes_app.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName="note")
public class Note
{
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name="title")
    String title;
    @ColumnInfo(name="desc")
    String desc;

    public Note(int id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    @Ignore
    public Note( String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

