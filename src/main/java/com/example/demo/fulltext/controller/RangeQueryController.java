package com.example.demo.fulltext.controller;

import com.example.demo.fulltext.model.ProductEntity;
import com.example.demo.fulltext.service.RangeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/range")
public class RangeQueryController {

    @Autowired
    private RangeQueryService rangeQueryService;

    // ===== NUMERIC RANGE ENDPOINTS =====

    // Endpoint cho price range query
    @GetMapping("/products/price")
    public ResponseEntity<List<ProductEntity>> findProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductEntity> products = rangeQueryService.findProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho quantity range query
    @GetMapping("/products/quantity")
    public ResponseEntity<List<ProductEntity>> findProductsByQuantityRange(
            @RequestParam Integer minQuantity,
            @RequestParam Integer maxQuantity) {
        List<ProductEntity> products = rangeQueryService.findProductsByQuantityRange(minQuantity, maxQuantity);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho rating range query
    @GetMapping("/products/rating")
    public ResponseEntity<List<ProductEntity>> findProductsByRatingRange(
            @RequestParam Double minRating,
            @RequestParam Double maxRating) {
        List<ProductEntity> products = rangeQueryService.findProductsByRatingRange(minRating, maxRating);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho weight range query
    @GetMapping("/products/weight")
    public ResponseEntity<List<ProductEntity>> findProductsByWeightRange(
            @RequestParam Double minWeight,
            @RequestParam Double maxWeight) {
        List<ProductEntity> products = rangeQueryService.findProductsByWeightRange(minWeight, maxWeight);
        return ResponseEntity.ok(products);
    }

    // ===== DATE RANGE ENDPOINTS =====

    // Endpoint cho created date range query
    @GetMapping("/products/created-date")
    public ResponseEntity<List<ProductEntity>> findProductsByCreatedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ProductEntity> products = rangeQueryService.findProductsByCreatedDateRange(startDate, endDate);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho last updated range query
    @GetMapping("/products/last-updated")
    public ResponseEntity<List<ProductEntity>> findProductsByLastUpdatedRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ProductEntity> products = rangeQueryService.findProductsByLastUpdatedRange(startDate, endDate);
        return ResponseEntity.ok(products);
    }

    // ===== GREATER THAN / LESS THAN ENDPOINTS =====

    // Endpoint cho price greater than
    @GetMapping("/products/price/greater-than")
    public ResponseEntity<List<ProductEntity>> findProductsPriceGreaterThan(
            @RequestParam Double price) {
        List<ProductEntity> products = rangeQueryService.findProductsPriceGreaterThan(price);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho price less than
    @GetMapping("/products/price/less-than")
    public ResponseEntity<List<ProductEntity>> findProductsPriceLessThan(
            @RequestParam Double price) {
        List<ProductEntity> products = rangeQueryService.findProductsPriceLessThan(price);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho rating greater than equal
    @GetMapping("/products/rating/greater-than-equal")
    public ResponseEntity<List<ProductEntity>> findProductsRatingGreaterThanEqual(
            @RequestParam Double rating) {
        List<ProductEntity> products = rangeQueryService.findProductsRatingGreaterThanEqual(rating);
        return ResponseEntity.ok(products);
    }

    // ===== COMBINATION ENDPOINTS =====

    // Endpoint cho price range với category filter
    @GetMapping("/products/price-category")
    public ResponseEntity<List<ProductEntity>> findProductsByPriceRangeAndCategory(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String category) {
        List<ProductEntity> products = rangeQueryService.findProductsByPriceRangeAndCategory(minPrice, maxPrice, category);
        return ResponseEntity.ok(products);
    }

    // Endpoint cho multiple range queries
    @GetMapping("/products/multiple-ranges")
    public ResponseEntity<List<ProductEntity>> findProductsByMultipleRanges(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam Integer minQuantity,
            @RequestParam Integer maxQuantity) {
        List<ProductEntity> products = rangeQueryService.findProductsByMultipleRanges(minPrice, maxPrice, minQuantity, maxQuantity);
        return ResponseEntity.ok(products);
    }

    // ===== PAGINATION ENDPOINTS =====

    // Endpoint cho price range với pagination
    @GetMapping("/products/price/paginated")
    public ResponseEntity<List<ProductEntity>> findProductsByPriceRangeWithPagination(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ProductEntity> products = rangeQueryService.findProductsByPriceRangeWithPagination(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(products);
    }

    // ===== REPOSITORY ENDPOINTS =====

    // Endpoint sử dụng repository method cho price range
    @GetMapping("/products/price/repository")
    public ResponseEntity<List<ProductEntity>> findProductsByPriceRangeUsingRepository(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductEntity> products = rangeQueryService.findProductsByPriceRangeUsingRepository(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    // Endpoint sử dụng repository method cho quantity range
    @GetMapping("/products/quantity/repository")
    public ResponseEntity<List<ProductEntity>> findProductsByQuantityRangeUsingRepository(
            @RequestParam Integer minQuantity,
            @RequestParam Integer maxQuantity) {
        List<ProductEntity> products = rangeQueryService.findProductsByQuantityRangeUsingRepository(minQuantity, maxQuantity);
        return ResponseEntity.ok(products);
    }

    // Endpoint sử dụng repository method cho rating range
    @GetMapping("/products/rating/repository")
    public ResponseEntity<List<ProductEntity>> findProductsByRatingRangeUsingRepository(
            @RequestParam Double minRating,
            @RequestParam Double maxRating) {
        List<ProductEntity> products = rangeQueryService.findProductsByRatingRangeUsingRepository(minRating, maxRating);
        return ResponseEntity.ok(products);
    }

    // ===== COUNT ENDPOINTS =====

    // Endpoint để đếm products trong price range
    @GetMapping("/products/price/count")
    public ResponseEntity<Long> countProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        long count = rangeQueryService.countProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(count);
    }

    // ===== DATA MANAGEMENT ENDPOINTS =====

    // Endpoint để tạo sample data cho ProductEntity
    @PostMapping("/sample-data")
    public ResponseEntity<String> createProductSampleData() {
        rangeQueryService.createProductSampleData();
        return ResponseEntity.ok("Product sample data created successfully!");
    }

    // Endpoint để thêm một product mới
    @PostMapping("/products/create")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        ProductEntity savedProduct = rangeQueryService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Endpoint để xóa tất cả products
    @DeleteMapping("/products")
    public ResponseEntity<String> deleteAllProducts() {
        rangeQueryService.deleteAllProducts();
        return ResponseEntity.ok("All products deleted successfully!");
    }

    // ===== ADVANCED ENDPOINTS =====

    // Endpoint demo price filtering cho e-commerce
    @GetMapping("/products/price-filter")
    public ResponseEntity<List<ProductEntity>> priceFilterDemo(
            @RequestParam(required = false) Double budget,
            @RequestParam(required = false) String priceCategory) {

        Double minPrice = 0.0;
        Double maxPrice = Double.MAX_VALUE;

        if (budget != null) {
            maxPrice = budget;
        }

        if (priceCategory != null) {
            switch (priceCategory.toLowerCase()) {
                case "budget":
                    maxPrice = 100.0;
                    break;
                case "mid-range":
                    minPrice = 100.0;
                    maxPrice = 500.0;
                    break;
                case "premium":
                    minPrice = 500.0;
                    maxPrice = 2000.0;
                    break;
                case "luxury":
                    minPrice = 2000.0;
                    break;
            }
        }

        List<ProductEntity> products = rangeQueryService.findProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    // Endpoint demo inventory management
    @GetMapping("/products/inventory-status")
    public ResponseEntity<String> inventoryStatusDemo() {
        long lowStock = rangeQueryService.countProductsByQuantityRange(1, 10);
        long mediumStock = rangeQueryService.countProductsByQuantityRange(11, 50);
        long highStock = rangeQueryService.countProductsByQuantityRange(51, Integer.MAX_VALUE);
        long outOfStock = rangeQueryService.countProductsByQuantityRange(0, 0);

        String report = String.format(
            "Inventory Status Report:\n" +
            "Out of Stock: %d products\n" +
            "Low Stock (1-10): %d products\n" +
            "Medium Stock (11-50): %d products\n" +
            "High Stock (51+): %d products",
            outOfStock, lowStock, mediumStock, highStock
        );

        return ResponseEntity.ok(report);
    }

    // Endpoint demo rating analysis
    @GetMapping("/products/rating-analysis")
    public ResponseEntity<String> ratingAnalysisDemo() {
        long excellent = rangeQueryService.countProductsByRatingRange(4.5, 5.0);
        long good = rangeQueryService.countProductsByRatingRange(4.0, 4.49);
        long average = rangeQueryService.countProductsByRatingRange(3.0, 3.99);
        long poor = rangeQueryService.countProductsByRatingRange(0.0, 2.99);

        String analysis = String.format(
            "Product Rating Analysis:\n" +
            "Excellent (4.5-5.0): %d products\n" +
            "Good (4.0-4.49): %d products\n" +
            "Average (3.0-3.99): %d products\n" +
            "Poor (0.0-2.99): %d products",
            excellent, good, average, poor
        );

        return ResponseEntity.ok(analysis);
    }
}
