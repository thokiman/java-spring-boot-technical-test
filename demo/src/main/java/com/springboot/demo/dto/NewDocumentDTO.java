package com.springboot.demo.dto;

public class NewDocumentDTO {

    private String author;
    private String query;
    private String sort;
    private String order;
    private int per_page;
    private int page;
    private String dir;

    public NewDocumentDTO() {
    }

    public NewDocumentDTO(String author, String query, String sort, String order, int per_page, int page, String dir) {
        this.author = author;
        this.query = query;
        this.sort = sort;
        this.order = order;
        this.per_page = per_page;
        this.page = page;
        this.dir = dir;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "NewDocumentRequest{" +
                "author='" + author + '\'' +
                ", query='" + query + '\'' +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                ", per_page=" + per_page +
                ", page=" + page +
                ", dir='" + dir + '\'' +
                '}';
    }
}
