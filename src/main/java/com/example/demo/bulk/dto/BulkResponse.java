package com.example.demo.bulk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkResponse {
    private boolean hasErrors;
    private long totalOperations;
    private long successfulOperations;
    private long failedOperations;
    private long processingTimeMs;
    private List<BulkOperationResult> results;
}
