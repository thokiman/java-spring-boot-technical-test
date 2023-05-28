package com.springboot.demo.map;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GithubUsers {
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("incomplete_results")
    private Boolean incompleteResults;

    @JsonProperty("items")
    private List<GithubUser> items;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<GithubUser> getItems() {
        return items;
    }

    public void setItems(List<GithubUser> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "GithubUsers{" +
                "totalCount=" + totalCount +
                ", incompleteResults=" + incompleteResults +
                ", items=" + items +
                '}';
    }
}
