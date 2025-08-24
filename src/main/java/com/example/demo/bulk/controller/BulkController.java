package com.example.demo.bulk.controller;

import com.example.demo.bulk.dto.BulkOperationRequest;
import com.example.demo.bulk.dto.BulkResponse;
import com.example.demo.bulk.dto.ProductDto;
import com.example.demo.bulk.model.Product;
import com.example.demo.bulk.service.BulkService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BulkController {

    private static final Logger log = LoggerFactory.getLogger(BulkController.class);
    private final BulkService bulkService;

    @PostMapping("/products/create")
    public ResponseEntity<BulkResponse> bulkCreateProducts(@RequestBody List<ProductDto> products) {
        log.info("Bulk creating {} products", products.size());
        BulkResponse response = bulkService.bulkCreateProducts(products);
        return ResponseEntity.status(response.isHasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/products/update")
    public ResponseEntity<BulkResponse> bulkUpdateProducts(@RequestBody List<ProductDto> products) {
        log.info("Bulk updating {} products", products.size());
        BulkResponse response = bulkService.bulkUpdateProducts(products);
        return ResponseEntity.status(response.isHasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/products/delete")
    public ResponseEntity<BulkResponse> bulkDeleteProducts(@RequestBody List<String> productIds) {
        log.info("Bulk deleting {} products", productIds.size());
        BulkResponse response = bulkService.bulkDeleteProducts(productIds);
        return ResponseEntity.status(response.isHasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/products/upsert")
    public ResponseEntity<BulkResponse> bulkUpsertProducts(@RequestBody List<ProductDto> products) {
        log.info("Bulk upserting {} products", products.size());
        BulkResponse response = bulkService.bulkUpsertProducts(products);
        return ResponseEntity.status(response.isHasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/products/operation")
    public ResponseEntity<BulkResponse> bulkOperation(@RequestBody BulkOperationRequest request) {
        log.info("Executing bulk operation: {} for {} products", request.getOperation(), request.getProducts().size());

        BulkResponse response;
        switch (request.getOperation().toUpperCase()) {
            case "CREATE":
                response = bulkService.bulkCreateProducts(request.getProducts());
                break;
            case "UPDATE":
                response = bulkService.bulkUpdateProducts(request.getProducts());
                break;
            case "UPSERT":
                response = bulkService.bulkUpsertProducts(request.getProducts());
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(response.isHasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .body(response);
    }

    // Additional endpoints for product management
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = bulkService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = bulkService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = bulkService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = bulkService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        List<Product> products = bulkService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/products/all")
    public ResponseEntity<Void> deleteAllProducts() {
        bulkService.deleteAllProducts();
        return ResponseEntity.noContent().build();
    }
}
