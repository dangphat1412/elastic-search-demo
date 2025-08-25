package com.example.demo.fulltext.dto;

import com.example.demo.fulltext.model.DocumentEntity;

import java.util.List;

public class SelectByIdsResponse {

    private List<DocumentEntity> documents;
    private long totalFound;
    private int requestedIds;
    private String queryType;
    private long executionTimeMs;
    private List<String> missingIds;

    public SelectByIdsResponse() {}

    public SelectByIdsResponse(List<DocumentEntity> documents, long totalFound, int requestedIds, String queryType) {
        this.documents = documents;
        this.totalFound = totalFound;
        this.requestedIds = requestedIds;
        this.queryType = queryType;
    }

    public SelectByIdsResponse(List<DocumentEntity> documents, long totalFound, int requestedIds,
                              String queryType, long executionTimeMs, List<String> missingIds) {
        this.documents = documents;
        this.totalFound = totalFound;
        this.requestedIds = requestedIds;
        this.queryType = queryType;
        this.executionTimeMs = executionTimeMs;
        this.missingIds = missingIds;
    }

    // Getters and Setters
    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public long getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(long totalFound) {
        this.totalFound = totalFound;
    }

    public int getRequestedIds() {
        return requestedIds;
    }

    public void setRequestedIds(int requestedIds) {
        this.requestedIds = requestedIds;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public List<String> getMissingIds() {
        return missingIds;
    }

    public void setMissingIds(List<String> missingIds) {
        this.missingIds = missingIds;
    }
}
