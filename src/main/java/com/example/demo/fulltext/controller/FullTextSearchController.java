package com.example.demo.fulltext.controller;

import com.example.demo.fulltext.model.Article;
import com.example.demo.fulltext.service.FullTextSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fulltext")
public class FullTextSearchController {

    @Autowired
    private FullTextSearchService fullTextSearchService;

    // Endpoint để test MatchAll query cơ bản
    @GetMapping("/match-all")
    public ResponseEntity<List<Article>> matchAllQuery() {
        List<Article> articles = fullTextSearchService.findAllArticles();
        return ResponseEntity.ok(articles);
    }

    // Endpoint để test MatchAll query với pagination
    @GetMapping("/match-all/paginated")
    public ResponseEntity<List<Article>> matchAllQueryWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Article> articles = fullTextSearchService.findAllArticlesWithPagination(page, size);
        return ResponseEntity.ok(articles);
    }

    // Endpoint để test MatchAll query với boost
    @GetMapping("/match-all/boost")
    public ResponseEntity<List<Article>> matchAllQueryWithBoost(
            @RequestParam(defaultValue = "1.0") float boost) {
        List<Article> articles = fullTextSearchService.findAllArticlesWithBoost(boost);
        return ResponseEntity.ok(articles);
    }

    // Endpoint để test MatchAll query kết hợp với filter
    @GetMapping("/match-all/category/{category}")
    public ResponseEntity<List<Article>> matchAllQueryByCategory(@PathVariable String category) {
        List<Article> articles = fullTextSearchService.findAllArticlesByCategory(category);
        return ResponseEntity.ok(articles);
    }

    // Endpoint sử dụng repository method đơn giản
    @GetMapping("/match-all/repository")
    public ResponseEntity<List<Article>> matchAllQueryUsingRepository() {
        List<Article> articles = fullTextSearchService.findAllArticlesUsingRepository();
        return ResponseEntity.ok(articles);
    }

    // Endpoint để đếm tổng số documents
    @GetMapping("/count")
    public ResponseEntity<Long> countAllArticles() {
        long count = fullTextSearchService.countAllArticles();
        return ResponseEntity.ok(count);
    }

    // Endpoint để tạo sample data
    @PostMapping("/sample-data")
    public ResponseEntity<String> createSampleData() {
        fullTextSearchService.createSampleData();
        return ResponseEntity.ok("Sample data created successfully!");
    }

    // Endpoint để thêm một article mới
    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article savedArticle = fullTextSearchService.saveArticle(article);
        return ResponseEntity.ok(savedArticle);
    }

    // Endpoint để xóa tất cả articles
    @DeleteMapping("/articles")
    public ResponseEntity<String> deleteAllArticles() {
        fullTextSearchService.deleteAllArticles();
        return ResponseEntity.ok("All articles deleted successfully!");
    }
}

