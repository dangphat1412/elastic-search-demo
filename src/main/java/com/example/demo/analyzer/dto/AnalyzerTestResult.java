package com.example.demo.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzerTestResult {
    private String testType;
    private String searchText;
    private String analyzer;
    private List<String> results;
    private int totalResults;
    private String description;
}
