package com.springboot.demo.response;

public class DocumentResponse {
    private String author;
    private String document;
    private String status;
    private String timestamp;

    private byte[] blob;

    public DocumentResponse() {
    }

    public DocumentResponse(String author, String document, String status, String timestamp, byte[] blob) {
        this.author = author;
        this.document = document;
        this.status = status;
        this.timestamp = timestamp;
        this.blob = blob;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }
}
