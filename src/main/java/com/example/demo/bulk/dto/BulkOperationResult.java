package com.example.demo.bulk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkOperationResult {
    private String id;
    private String operation;
    private boolean success;
    private String error;
    private int status;
}
