package com.example.demo.analyzer.controller;

import com.example.demo.analyzer.dto.AnalyzerTestResult;
import com.example.demo.analyzer.model.AnalyzerDocument;
import com.example.demo.analyzer.service.AnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/analyzer")
@RequiredArgsConstructor
public class AnalyzerController {

    private final AnalyzerService analyzerService;

    @PostMapping("/init")
    public ResponseEntity<String> initSampleData() {
        try {
            analyzerService.createSampleData();
            return ResponseEntity.ok("Sample data created successfully for analyzer testing!");
        } catch (Exception e) {
            log.error("Error creating sample data", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/test/{text}")
    public ResponseEntity<List<AnalyzerTestResult>> testAnalyzers(@PathVariable String text) {
        try {
            List<AnalyzerTestResult> results = new ArrayList<>();

            // Test Standard Analyzer
            var standardResults = analyzerService.multiMatchSearch(text);
            AnalyzerTestResult standardResult = new AnalyzerTestResult();
            standardResult.setTestType("Standard Analyzer");
            standardResult.setSearchText(text);
            standardResult.setAnalyzer("standard");
            standardResult.setResults(standardResults.stream().map(AnalyzerDocument::getStandardText).toList());
            standardResult.setTotalResults(standardResults.size());
            standardResult.setDescription("Phân tích văn bản thành tokens, loại bỏ dấu câu, chuyển thành chữ thường");
            results.add(standardResult);

            // Test Autocomplete
            var autocompleteResults = analyzerService.testAutocomplete(text);
            AnalyzerTestResult autocompleteResult = new AnalyzerTestResult();
            autocompleteResult.setTestType("Autocomplete");
            autocompleteResult.setSearchText(text);
            autocompleteResult.setAnalyzer("autocomplete_index");
            autocompleteResult.setResults(autocompleteResults.stream().map(AnalyzerDocument::getAutocompleteText).toList());
            autocompleteResult.setTotalResults(autocompleteResults.size());
            autocompleteResult.setDescription("Sử dụng edge n-gram để tạo tính năng autocomplete");
            results.add(autocompleteResult);

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error testing analyzers", e);
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/fuzzy/{text}")
    public ResponseEntity<AnalyzerTestResult> fuzzySearch(
            @PathVariable String text,
            @RequestParam(defaultValue = "standardText") String field) {
        try {
            var results = analyzerService.fuzzySearch(text, field);
            AnalyzerTestResult result = new AnalyzerTestResult();
            result.setTestType("Fuzzy Search");
            result.setSearchText(text);
            result.setAnalyzer("fuzzy");
            result.setResults(results.stream().map(doc ->
                switch(field) {
                    case "standardText" -> doc.getStandardText();
                    case "title" -> doc.getTitle();
                    case "vietnameseText" -> doc.getVietnameseText();
                    default -> doc.getStandardText();
                }
            ).toList());
            result.setTotalResults(results.size());
            result.setDescription("Tìm kiếm gần đúng, cho phép sai sót chính tả (fuzziness)");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in fuzzy search", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/wildcard/{pattern}")
    public ResponseEntity<AnalyzerTestResult> wildcardSearch(
            @PathVariable String pattern,
            @RequestParam(defaultValue = "standardText") String field) {
        try {
            var results = analyzerService.wildcardSearch(pattern, field);
            AnalyzerTestResult result = new AnalyzerTestResult();
            result.setTestType("Wildcard Search");
            result.setSearchText(pattern);
            result.setAnalyzer("wildcard");
            result.setResults(results.stream().map(doc ->
                switch(field) {
                    case "standardText" -> doc.getStandardText();
                    case "title" -> doc.getTitle();
                    case "vietnameseText" -> doc.getVietnameseText();
                    default -> doc.getStandardText();
                }
            ).toList());
            result.setTotalResults(results.size());
            result.setDescription("Tìm kiếm với ký tự đại diện: * (nhiều ký tự), ? (một ký tự)");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in wildcard search", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/boolean")
    public ResponseEntity<AnalyzerTestResult> booleanSearch(
            @RequestParam String mustText,
            @RequestParam(required = false) String shouldText,
            @RequestParam(required = false) String mustNotText) {
        try {
            var results = analyzerService.booleanSearch(mustText, shouldText, mustNotText);
            AnalyzerTestResult result = new AnalyzerTestResult();
            result.setTestType("Boolean Search");
            result.setSearchText(String.format("MUST: %s, SHOULD: %s, MUST_NOT: %s", mustText, shouldText, mustNotText));
            result.setAnalyzer("boolean");
            result.setResults(results.stream().map(AnalyzerDocument::getStandardText).toList());
            result.setTotalResults(results.size());
            result.setDescription("Kết hợp nhiều điều kiện: MUST (bắt buộc), SHOULD (tùy chọn), MUST_NOT (loại trừ)");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in boolean search", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/range")
    public ResponseEntity<AnalyzerTestResult> rangeSearch(
            @RequestParam String fromId,
            @RequestParam String toId) {
        try {
            var results = analyzerService.rangeSearchById(fromId, toId);
            AnalyzerTestResult result = new AnalyzerTestResult();
            result.setTestType("Range Search");
            result.setSearchText(String.format("From: %s To: %s", fromId, toId));
            result.setAnalyzer("range");
            result.setResults(results.stream().map(AnalyzerDocument::getId).toList());
            result.setTotalResults(results.size());
            result.setDescription("Tìm kiếm trong khoảng giá trị (range query)");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in range search", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnalyzerDocument>> getAllDocuments() {
        try {
            return ResponseEntity.ok(analyzerService.getAllDocuments());
        } catch (Exception e) {
            log.error("Error getting all documents", e);
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllData() {
        try {
            analyzerService.deleteAllDocuments();
            return ResponseEntity.ok("All data cleared successfully!");
        } catch (Exception e) {
            log.error("Error clearing data", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/demo")
    public ResponseEntity<String> getDemoInstructions() {
        String instructions = """
            === ELASTICSEARCH ANALYZER DEMO ===
            
            1. Khởi tạo dữ liệu mẫu:
               POST /api/analyzer/init
            
            2. Test các analyzer:
               GET /api/analyzer/test/{text}
               Ví dụ: GET /api/analyzer/test/java
            
            3. Fuzzy search (tìm kiếm gần đúng):
               GET /api/analyzer/fuzzy/{text}?field=standardText
               Ví dụ: GET /api/analyzer/fuzzy/jav?field=standardText
            
            4. Wildcard search:
               GET /api/analyzer/wildcard/{pattern}?field=standardText
               Ví dụ: GET /api/analyzer/wildcard/java*?field=standardText
            
            5. Boolean search:
               POST /api/analyzer/boolean?mustText=java&shouldText=spring&mustNotText=python
            
            6. Range search:
               GET /api/analyzer/range?fromId=1&toId=3
            
            7. Xem tất cả documents:
               GET /api/analyzer/all
            
            8. Xóa tất cả dữ liệu:
               DELETE /api/analyzer/clear
            
            === CÁC ANALYZER ĐƯỢC SỬ DỤNG ===
            - Standard: Phân tích tiêu chuẩn
            - Keyword: Giữ nguyên văn bản
            - Simple: Chỉ lowercase và tách bằng non-letter
            - Whitespace: Chỉ tách bằng khoảng trắng
            - Stop: Loại bỏ stop words
            - Vietnamese: Phân tích tiếng Việt
            - Custom: Analyzer tùy chỉnh
            - Autocomplete: Edge n-gram cho autocomplete
            - Email: Analyzer cho email
            """;
        return ResponseEntity.ok(instructions);
    }
}
