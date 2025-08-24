package com.example.demo.analyzer.repository;

import com.example.demo.analyzer.model.AnalyzerDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyzerRepository extends ElasticsearchRepository<AnalyzerDocument, String> {

    // Tìm kiếm bằng standard analyzer
    List<AnalyzerDocument> findByStandardTextContaining(String text);

    // Tìm kiếm exact match với keyword analyzer
    List<AnalyzerDocument> findByKeywordText(String text);

    // Tìm kiếm với simple analyzer
    List<AnalyzerDocument> findBySimpleTextContaining(String text);

    // Tìm kiếm với Vietnamese analyzer
    List<AnalyzerDocument> findByVietnameseTextContaining(String text);

    // Tìm kiếm autocomplete
    List<AnalyzerDocument> findByAutocompleteTextContaining(String text);

    // Tìm kiếm theo category
    List<AnalyzerDocument> findByCategory(String category);

    // Tìm kiếm theo title
    List<AnalyzerDocument> findByTitleContaining(String title);
}
