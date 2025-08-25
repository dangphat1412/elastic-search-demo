package com.example.demo.fulltext.dto;

import com.example.demo.fulltext.model.Article;

import java.util.List;

public class SearchResponse {

    private List<Article> articles;
    private long totalHits;
    private int page;
    private int size;
    private String queryType;
    private long searchTimeMs;

    public SearchResponse() {}

    public SearchResponse(List<Article> articles, long totalHits, String queryType) {
        this.articles = articles;
        this.totalHits = totalHits;
        this.queryType = queryType;
    }

    public SearchResponse(List<Article> articles, long totalHits, int page, int size, String queryType, long searchTimeMs) {
        this.articles = articles;
        this.totalHits = totalHits;
        this.page = page;
        this.size = size;
        this.queryType = queryType;
        this.searchTimeMs = searchTimeMs;
    }

    // Getters and Setters
    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public long getSearchTimeMs() {
        return searchTimeMs;
    }

    public void setSearchTimeMs(long searchTimeMs) {
        this.searchTimeMs = searchTimeMs;
    }
}
