package com.ahnaftn.mynotes;
import com.google.firebase.Timestamp;


public class Note {
    private String title;
    private String content;
    private Timestamp timestamp;

    public Note() {

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //public void setTimestamp(Timestamp timestamp) {
    //    this.timestamp = timestamp;
    //}
}
