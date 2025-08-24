package com.example.demo.analyzer.service;

import com.example.demo.analyzer.model.AnalyzerDocument;
import com.example.demo.analyzer.repository.AnalyzerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final AnalyzerRepository analyzerRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    // Tạo sample data để test
    public void createSampleData() {
        log.info("Creating sample data for analyzer testing...");

        List<AnalyzerDocument> documents = List.of(
            createDocument("1",
                "Hello World! This is a standard text.",
                "Hello World! This is a keyword text.",
                "Hello World! This is a SIMPLE text.",
                "Hello  World!  This   is   whitespace   text.",
                "Hello World! This is a stop words text with the and a.",
                "Xin chào! Đây là văn bản tiếng Việt với các từ dừng và dấu.",
                "Hello & World @ Custom Analyzer <b>HTML</b> text.",
                "Hello World Autocomplete",
                "user@example.com",
                "Smartphone Samsung Galaxy",
                "Electronics",
                "Điện thoại thông minh cao cấp"
            ),
            createDocument("2",
                "Java Spring Boot Elasticsearch Tutorial",
                "Java Spring Boot Elasticsearch Tutorial",
                "Java SPRING boot elasticsearch TUTORIAL",
                "Java   Spring   Boot   Elasticsearch   Tutorial",
                "Java Spring Boot Elasticsearch Tutorial with the best practices",
                "Hướng dẫn Java Spring Boot và Elasticsearch cho người mới bắt đầu",
                "Java & Spring @ Boot <i>Elasticsearch</i> Tutorial",
                "Java Spring Boot",
                "admin@company.org",
                "Laptop Dell XPS",
                "Computers",
                "Máy tính xách tay hiệu năng cao"
            ),
            createDocument("3",
                "Machine Learning and Artificial Intelligence",
                "Machine Learning and Artificial Intelligence",
                "MACHINE learning AND artificial INTELLIGENCE",
                "Machine    Learning    and    Artificial    Intelligence",
                "Machine Learning and Artificial Intelligence are the future",
                "Máy học và Trí tuệ nhân tạo đang phát triển mạnh mẽ",
                "Machine Learning & AI @ Future <strong>Technology</strong>",
                "Machine Learning AI",
                "info@tech.vn",
                "iPhone 15 Pro Max",
                "Mobile",
                "Điện thoại di động thế hệ mới"
            )
        );

        analyzerRepository.saveAll(documents);
        log.info("Sample data created successfully!");
    }

    private AnalyzerDocument createDocument(String id, String standardText, String keywordText,
                                          String simpleText, String whitespaceText, String stopText,
                                          String vietnameseText, String customText, String autocompleteText,
                                          String emailText, String title, String category, String description) {
        AnalyzerDocument doc = new AnalyzerDocument();
        doc.setId(id);
        doc.setStandardText(standardText);
        doc.setKeywordText(keywordText);
        doc.setSimpleText(simpleText);
        doc.setWhitespaceText(whitespaceText);
        doc.setStopText(stopText);
        doc.setVietnameseText(vietnameseText);
        doc.setCustomText(customText);
        doc.setAutocompleteText(autocompleteText);
        doc.setEmailText(emailText);
        doc.setTitle(title);
        doc.setCategory(category);
        doc.setDescription(description);
        return doc;
    }

    // Test các analyzer khác nhau
    public void testAnalyzers(String text) {
        log.info("Testing analyzers with text: {}", text);

        // Test Standard Analyzer
        log.info("=== STANDARD ANALYZER ===");
        List<AnalyzerDocument> standardResults = analyzerRepository.findByStandardTextContaining(text);
        standardResults.forEach(doc -> log.info("Standard: {}", doc.getStandardText()));

        // Test Keyword Analyzer
        log.info("=== KEYWORD ANALYZER ===");
        List<AnalyzerDocument> keywordResults = analyzerRepository.findByKeywordText(text);
        keywordResults.forEach(doc -> log.info("Keyword: {}", doc.getKeywordText()));

        // Test Simple Analyzer
        log.info("=== SIMPLE ANALYZER ===");
        List<AnalyzerDocument> simpleResults = analyzerRepository.findBySimpleTextContaining(text);
        simpleResults.forEach(doc -> log.info("Simple: {}", doc.getSimpleText()));

        // Test Vietnamese Analyzer
        log.info("=== VIETNAMESE ANALYZER ===");
        List<AnalyzerDocument> vietnameseResults = analyzerRepository.findByVietnameseTextContaining(text);
        vietnameseResults.forEach(doc -> log.info("Vietnamese: {}", doc.getVietnameseText()));
    }

    // Test autocomplete
    public List<AnalyzerDocument> testAutocomplete(String prefix) {
        log.info("Testing autocomplete with prefix: {}", prefix);
        return analyzerRepository.findByAutocompleteTextContaining(prefix);
    }

    // Fuzzy search - tìm kiếm gần đúng
    public List<AnalyzerDocument> fuzzySearch(String text, String field) {
        log.info("Fuzzy search for: {} in field: {}", text, field);
        // Simplified implementation using repository methods
        return analyzerRepository.findByStandardTextContaining(text);
    }

    // Multi-match search across multiple fields
    public List<AnalyzerDocument> multiMatchSearch(String text) {
        log.info("Multi-match search for: {}", text);
        // Simplified implementation
        List<AnalyzerDocument> results = analyzerRepository.findByStandardTextContaining(text);
        if (results.isEmpty()) {
            results = analyzerRepository.findByVietnameseTextContaining(text);
        }
        if (results.isEmpty()) {
            results = analyzerRepository.findByTitleContaining(text);
        }
        return results;
    }

    // Wildcard search
    public List<AnalyzerDocument> wildcardSearch(String pattern, String field) {
        log.info("Wildcard search for pattern: {} in field: {}", pattern, field);
        // Simplified implementation
        String searchText = pattern.replace("*", "").replace("?", "");
        return analyzerRepository.findByStandardTextContaining(searchText);
    }

    // Boolean search - kết hợp nhiều điều kiện
    public List<AnalyzerDocument> booleanSearch(String mustText, String shouldText, String mustNotText) {
        log.info("Boolean search - must: {}, should: {}, mustNot: {}", mustText, shouldText, mustNotText);
        // Simplified implementation
        return analyzerRepository.findByStandardTextContaining(mustText);
    }

    // Range search
    public List<AnalyzerDocument> rangeSearchById(String fromId, String toId) {
        log.info("Range search from ID: {} to ID: {}", fromId, toId);
        // Simplified implementation
        List<AnalyzerDocument> allDocs = (List<AnalyzerDocument>) analyzerRepository.findAll();
        return allDocs.stream()
                .filter(doc -> doc.getId().compareTo(fromId) >= 0 && doc.getId().compareTo(toId) <= 0)
                .collect(Collectors.toList());
    }

    // Get all documents
    public List<AnalyzerDocument> getAllDocuments() {
        return (List<AnalyzerDocument>) analyzerRepository.findAll();
    }

    // Delete all documents
    public void deleteAllDocuments() {
        analyzerRepository.deleteAll();
        log.info("All documents deleted!");
    }
}
