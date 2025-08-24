package com.example.demo.bulk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkOperationRequest {
    private List<ProductDto> products;
    private String operation; // CREATE, UPDATE, DELETE
}
