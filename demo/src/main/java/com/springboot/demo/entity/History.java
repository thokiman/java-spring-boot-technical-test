package com.springboot.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Blob;
import java.sql.Timestamp;


@Entity
@Table(name="histories")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="doc_author", columnDefinition = "VARCHAR(255)", nullable=false)
    private String docAuthor;

    @Column(name="doc_name", columnDefinition = "VARCHAR(255)", nullable=false)
    private String docName;

    @Lob
    @Column(name = "doc_bin", columnDefinition="BLOB", nullable=true)
    private Blob docBin;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public History() {
    }

    public History(String docAuthor, String docName, Blob docBin) {
        this.docAuthor = docAuthor;
        this.docName = docName;
        this.docBin = docBin;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocAuthor() {
        return docAuthor;
    }

    public void setDocAuthor(String docAuthor) {
        this.docAuthor = docAuthor;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Blob getDocBin() {
        return docBin;
    }

    public void setDocBin(Blob docBin) {
        this.docBin = docBin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", docAuthor='" + docAuthor + '\'' +
                ", docName='" + docName + '\'' +
                ", docBin=" + docBin +
                ", createdAt=" + createdAt +
                '}';
    }
}
