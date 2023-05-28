package com.springboot.demo.dto;

public class OldDocumentDTO {
    private String dir;

    public OldDocumentDTO() {
    }

    public OldDocumentDTO(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "OldDocumentRequest{" +
                "dir='" + dir + '\'' +
                '}';
    }
}
