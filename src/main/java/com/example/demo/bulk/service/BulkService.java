package com.example.demo.bulk.service;

import com.example.demo.bulk.dto.*;
import com.example.demo.bulk.model.Product;
import com.example.demo.bulk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkService {

    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public BulkResponse bulkCreateProducts(List<ProductDto> productDtos) {
        long startTime = System.currentTimeMillis();
        List<BulkOperationResult> results = new ArrayList<>();

        List<IndexQuery> queries = new ArrayList<>();

        for (ProductDto dto : productDtos) {
            try {
                Product product = convertToProduct(dto);
                product.setId(UUID.randomUUID().toString());
                product.setCreatedAt(LocalDate.now());
                product.setUpdatedAt(LocalDate.now());

                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(product.getId())
                        .withObject(product)
                        .build();

                queries.add(indexQuery);

                results.add(new BulkOperationResult(product.getId(), "CREATE", true, null, 201));

            } catch (Exception e) {
                log.error("Error creating product: {}", e.getMessage());
                results.add(new BulkOperationResult(null, "CREATE", false, e.getMessage(), 400));
            }
        }

        try {
            elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of("products"));
        } catch (Exception e) {
            log.error("Bulk index operation failed: {}", e.getMessage());
            // Mark all operations as failed
            results.forEach(result -> {
                result.setSuccess(false);
                result.setError(e.getMessage());
                result.setStatus(500);
            });
        }

        return buildBulkResponse(results, startTime);
    }

    public BulkResponse bulkUpdateProducts(List<ProductDto> productDtos) {
        long startTime = System.currentTimeMillis();
        List<BulkOperationResult> results = new ArrayList<>();

        List<IndexQuery> queries = new ArrayList<>();

        for (ProductDto dto : productDtos) {
            try {
                if (dto.getId() == null) {
                    results.add(new BulkOperationResult(null, "UPDATE", false, "Product ID is required for update", 400));
                    continue;
                }

                Optional<Product> existingProduct = productRepository.findById(dto.getId());
                if (existingProduct.isEmpty()) {
                    results.add(new BulkOperationResult(dto.getId(), "UPDATE", false, "Product not found", 404));
                    continue;
                }

                Product product = existingProduct.get();
                updateProductFromDto(product, dto);
                product.setUpdatedAt(LocalDate.now());

                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(product.getId())
                        .withObject(product)
                        .build();

                queries.add(indexQuery);
                results.add(new BulkOperationResult(product.getId(), "UPDATE", true, null, 200));

            } catch (Exception e) {
                log.error("Error updating product {}: {}", dto.getId(), e.getMessage());
                results.add(new BulkOperationResult(dto.getId(), "UPDATE", false, e.getMessage(), 400));
            }
        }

        try {
            if (!queries.isEmpty()) {
                elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of("products"));
            }
        } catch (Exception e) {
            log.error("Bulk update operation failed: {}", e.getMessage());
            results.forEach(result -> {
                if (result.isSuccess()) {
                    result.setSuccess(false);
                    result.setError(e.getMessage());
                    result.setStatus(500);
                }
            });
        }

        return buildBulkResponse(results, startTime);
    }

    public BulkResponse bulkDeleteProducts(List<String> productIds) {
        long startTime = System.currentTimeMillis();
        List<BulkOperationResult> results = new ArrayList<>();

        for (String productId : productIds) {
            try {
                if (productRepository.existsById(productId)) {
                    productRepository.deleteById(productId);
                    results.add(new BulkOperationResult(productId, "DELETE", true, null, 200));
                } else {
                    results.add(new BulkOperationResult(productId, "DELETE", false, "Product not found", 404));
                }
            } catch (Exception e) {
                log.error("Error deleting product {}: {}", productId, e.getMessage());
                results.add(new BulkOperationResult(productId, "DELETE", false, e.getMessage(), 400));
            }
        }

        return buildBulkResponse(results, startTime);
    }

    public BulkResponse bulkUpsertProducts(List<ProductDto> productDtos) {
        long startTime = System.currentTimeMillis();
        List<BulkOperationResult> results = new ArrayList<>();
        List<IndexQuery> queries = new ArrayList<>();

        for (ProductDto dto : productDtos) {
            try {
                BulkOperationResult result = processUpsertProduct(dto, queries);
                results.add(result);
            } catch (Exception e) {
                log.error("Error upserting product {}: {}", dto.getId(), e.getMessage());
                results.add(new BulkOperationResult(dto.getId(), "UPSERT", false, e.getMessage(), 400));
            }
        }

        try {
            if (!queries.isEmpty()) {
                elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of("products"));
            }
        } catch (Exception e) {
            log.error("Bulk upsert operation failed: {}", e.getMessage());
            results.forEach(result -> {
                if (result.isSuccess()) {
                    result.setSuccess(false);
                    result.setError(e.getMessage());
                    result.setStatus(500);
                }
            });
        }

        return buildBulkResponse(results, startTime);
    }

    private BulkOperationResult processUpsertProduct(ProductDto dto, List<IndexQuery> queries) {
        Product product;
        String operation;

        if (dto.getId() != null && productRepository.existsById(dto.getId())) {
            // Update existing product
            Optional<Product> existingProduct = productRepository.findById(dto.getId());
            if (existingProduct.isPresent()) {
                product = existingProduct.get();
                updateProductFromDto(product, dto);
                product.setUpdatedAt(LocalDate.now());
                operation = "UPDATE";
            } else {
                return new BulkOperationResult(dto.getId(), "UPSERT", false, "Product not found", 404);
            }
        } else {
            // Create new product
            product = convertToProduct(dto);
            if (product.getId() == null) {
                product.setId(UUID.randomUUID().toString());
            }
            product.setCreatedAt(LocalDate.now());
            product.setUpdatedAt(LocalDate.now());
            operation = "CREATE";
        }

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(product.getId())
                .withObject(product)
                .build();

        queries.add(indexQuery);

        return new BulkOperationResult(product.getId(), operation, true, null, operation.equals("CREATE") ? 201 : 200);
    }

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    private Product convertToProduct(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setActive(dto.getActive() != null ? dto.getActive() : Boolean.TRUE);
        product.setBrand(dto.getBrand());
        product.setSku(dto.getSku());
        return product;
    }

    private void updateProductFromDto(Product product, ProductDto dto) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getStock() != null) product.setStock(dto.getStock());
        if (dto.getActive() != null) product.setActive(dto.getActive());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getSku() != null) product.setSku(dto.getSku());
    }

    private BulkResponse buildBulkResponse(List<BulkOperationResult> results, long startTime) {
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        long successfulOperations = results.stream().mapToLong(r -> r.isSuccess() ? 1 : 0).sum();
        long failedOperations = results.size() - successfulOperations;
        boolean hasErrors = failedOperations > 0;

        return new BulkResponse(
                hasErrors,
                results.size(),
                successfulOperations,
                failedOperations,
                processingTime,
                results
        );
    }
}

