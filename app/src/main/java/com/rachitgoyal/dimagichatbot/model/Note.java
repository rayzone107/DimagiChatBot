package com.rachitgoyal.dimagichatbot.model;

import com.orm.dsl.Table;

@Table
public class Note {

    private long timestamp;
    private String text;
    private String booklet;

    public Note() {
    }

    public Note(long timestamp, String text, String booklet) {
        this.timestamp = timestamp;
        this.text = text;
        this.booklet = booklet;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBooklet() {
        return booklet;
    }

    public void setBooklet(String booklet) {
        this.booklet = booklet;
    }
}
