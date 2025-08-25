package com.example.demo.fulltext.dto;

import com.example.demo.fulltext.model.ProductEntity;

import java.util.List;

public class RangeQueryResponse {

    private List<ProductEntity> products;
    private long totalFound;
    private String rangeField;
    private Object minValue;
    private Object maxValue;
    private String queryType;
    private long executionTimeMs;
    private RangeStatistics statistics;

    public RangeQueryResponse() {}

    public RangeQueryResponse(List<ProductEntity> products, long totalFound, String rangeField,
                             Object minValue, Object maxValue, String queryType) {
        this.products = products;
        this.totalFound = totalFound;
        this.rangeField = rangeField;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.queryType = queryType;
    }

    // Inner class for range statistics
    public static class RangeStatistics {
        private Object actualMin;
        private Object actualMax;
        private Object average;
        private long count;

        public RangeStatistics() {}

        public RangeStatistics(Object actualMin, Object actualMax, Object average, long count) {
            this.actualMin = actualMin;
            this.actualMax = actualMax;
            this.average = average;
            this.count = count;
        }

        // Getters and Setters
        public Object getActualMin() { return actualMin; }
        public void setActualMin(Object actualMin) { this.actualMin = actualMin; }
        public Object getActualMax() { return actualMax; }
        public void setActualMax(Object actualMax) { this.actualMax = actualMax; }
        public Object getAverage() { return average; }
        public void setAverage(Object average) { this.average = average; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    // Getters and Setters
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public long getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(long totalFound) {
        this.totalFound = totalFound;
    }

    public String getRangeField() {
        return rangeField;
    }

    public void setRangeField(String rangeField) {
        this.rangeField = rangeField;
    }

    public Object getMinValue() {
        return minValue;
    }

    public void setMinValue(Object minValue) {
        this.minValue = minValue;
    }

    public Object getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Object maxValue) {
        this.maxValue = maxValue;
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

    public RangeStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(RangeStatistics statistics) {
        this.statistics = statistics;
    }
}
