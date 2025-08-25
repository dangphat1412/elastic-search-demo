package com.example.demo.fulltext.controller;

import com.example.demo.fulltext.model.DocumentEntity;
import com.example.demo.fulltext.service.SelectByIdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/selectbyids")
public class SelectByIdsController {

    @Autowired
    private SelectByIdsService selectByIdsService;

    // ===== BASIC SELECT BY IDS ENDPOINTS =====

    // Endpoint để test SelectByIds query cơ bản
    @PostMapping("/documents")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIds(@RequestBody List<String> ids) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIds(ids);
        return ResponseEntity.ok(documents);
    }

    // Endpoint để test SelectByIds với pagination
    @PostMapping("/documents/paginated")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsWithPagination(
            @RequestBody List<String> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsWithPagination(ids, page, size);
        return ResponseEntity.ok(documents);
    }

    // Endpoint để test SelectByIds với boost
    @PostMapping("/documents/boost")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsWithBoost(
            @RequestBody List<String> ids,
            @RequestParam(defaultValue = "1.0") float boost) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsWithBoost(ids, boost);
        return ResponseEntity.ok(documents);
    }

    // ===== FILTER ENDPOINTS =====

    // Endpoint để test SelectByIds kết hợp với filter theo type
    @PostMapping("/documents/type/{type}")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsAndType(
            @RequestBody List<String> ids,
            @PathVariable String type) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsAndType(ids, type);
        return ResponseEntity.ok(documents);
    }

    // Endpoint để test SelectByIds kết hợp với filter theo status
    @PostMapping("/documents/status/{status}")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsAndStatus(
            @RequestBody List<String> ids,
            @PathVariable String status) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsAndStatus(ids, status);
        return ResponseEntity.ok(documents);
    }

    // Endpoint để test SelectByIds với multiple filters
    @PostMapping("/documents/filters")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsWithMultipleFilters(
            @RequestBody List<String> ids,
            @RequestParam String type,
            @RequestParam String status) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsWithMultipleFilters(ids, type, status);
        return ResponseEntity.ok(documents);
    }

    // ===== REPOSITORY AND UTILITY ENDPOINTS =====

    // Endpoint sử dụng repository method đơn giản
    @PostMapping("/documents/repository")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsUsingRepository(@RequestBody List<String> ids) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsUsingRepository(ids);
        return ResponseEntity.ok(documents);
    }

    // Endpoint để đếm số documents theo IDs
    @PostMapping("/documents/count")
    public ResponseEntity<Long> countDocumentsByIds(@RequestBody List<String> ids) {
        long count = selectByIdsService.countDocumentsByIds(ids);
        return ResponseEntity.ok(count);
    }

    // Endpoint để lấy tất cả IDs trong index
    @GetMapping("/documents/all-ids")
    public ResponseEntity<List<String>> getAllDocumentIds() {
        List<String> ids = selectByIdsService.getAllDocumentIds();
        return ResponseEntity.ok(ids);
    }

    // Endpoint để kiểm tra IDs nào tồn tại
    @PostMapping("/documents/check-existing")
    public ResponseEntity<List<String>> checkExistingDocumentIds(@RequestBody List<String> ids) {
        List<String> existingIds = selectByIdsService.checkExistingDocumentIds(ids);
        return ResponseEntity.ok(existingIds);
    }

    // Endpoint để lấy documents theo IDs và sắp xếp theo priority
    @PostMapping("/documents/ordered-by-priority")
    public ResponseEntity<List<DocumentEntity>> selectDocumentsByIdsOrderByPriority(@RequestBody List<String> ids) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIdsOrderByPriority(ids);
        return ResponseEntity.ok(documents);
    }

    // ===== DATA MANAGEMENT ENDPOINTS =====

    // Endpoint để tạo sample data cho DocumentEntity
    @PostMapping("/sample-data")
    public ResponseEntity<String> createDocumentSampleData() {
        selectByIdsService.createDocumentSampleData();
        return ResponseEntity.ok("Document sample data created successfully!");
    }

    // Endpoint để thêm một document mới
    @PostMapping("/documents/create")
    public ResponseEntity<DocumentEntity> createDocument(@RequestBody DocumentEntity document) {
        DocumentEntity savedDocument = selectByIdsService.saveDocument(document);
        return ResponseEntity.ok(savedDocument);
    }

    // Endpoint để xóa tất cả documents
    @DeleteMapping("/documents")
    public ResponseEntity<String> deleteAllDocuments() {
        selectByIdsService.deleteAllDocuments();
        return ResponseEntity.ok("All documents deleted successfully!");
    }

    // ===== ADVANCED ENDPOINTS =====

    // Endpoint để demo batch processing với SelectByIds
    @PostMapping("/documents/batch-process")
    public ResponseEntity<String> batchProcessDocuments(@RequestBody List<String> ids) {
        List<DocumentEntity> documents = selectByIdsService.findDocumentsByIds(ids);

        // Simulate batch processing
        int processedCount = 0;
        for (DocumentEntity doc : documents) {
            // Simulate some processing (e.g., updating score)
            doc.setScore(doc.getScore() + 1.0);
            selectByIdsService.saveDocument(doc);
            processedCount++;
        }

        return ResponseEntity.ok("Batch processed " + processedCount + " documents successfully!");
    }

    // Endpoint để demo performance comparison
    @PostMapping("/documents/performance-test")
    public ResponseEntity<String> performanceTest(@RequestBody List<String> ids) {
        long startTime = System.currentTimeMillis();

        // Test với SelectByIds query
        List<DocumentEntity> documents1 = selectByIdsService.findDocumentsByIds(ids);
        long selectByIdsTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();

        // Test với Repository method
        List<DocumentEntity> documents2 = selectByIdsService.findDocumentsByIdsUsingRepository(ids);
        long repositoryTime = System.currentTimeMillis() - startTime;

        String result = String.format(
            "Performance Test Results:\n" +
            "SelectByIds Query: %d ms (found %d documents)\n" +
            "Repository Method: %d ms (found %d documents)",
            selectByIdsTime, documents1.size(),
            repositoryTime, documents2.size()
        );

        return ResponseEntity.ok(result);
    }
}
