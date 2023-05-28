package com.springboot.demo.response;

public class HistoryResponse {

    private String author;
    private String document;
    private String created_at;

    public HistoryResponse() {
    }

    public HistoryResponse(String author, String document, String created_at) {
        this.author = author;
        this.document = document;
        this.created_at = created_at;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "HistoryResponse{" +
                "author='" + author + '\'' +
                ", document='" + document + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
