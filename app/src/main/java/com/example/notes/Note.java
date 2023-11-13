package com.example.notes;

import java.io.Serializable;

public class Note implements Serializable {
    private String header;
    private String body;

    public Note(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String toString() {
        return header + "\n" + body;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
