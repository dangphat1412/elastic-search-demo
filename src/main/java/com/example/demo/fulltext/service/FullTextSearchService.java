package com.example.demo.fulltext.service;

import com.example.demo.fulltext.model.Article;
import com.example.demo.fulltext.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FullTextSearchService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    // MatchAll query - Tìm tất cả documents
    public List<Article> findAllArticles() {
        Query matchAllQuery = MatchAllQuery.of(m -> m)._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // MatchAll query với pagination
    public List<Article> findAllArticlesWithPagination(int page, int size) {
        Query matchAllQuery = MatchAllQuery.of(m -> m)._toQuery();
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .withPageable(pageable)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // MatchAll query với boost (tăng relevance score)
    public List<Article> findAllArticlesWithBoost(float boost) {
        Query matchAllQuery = MatchAllQuery.of(m -> m.boost(boost))._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Kết hợp MatchAll với filter
    public List<Article> findAllArticlesByCategory(String category) {
        Query matchAllQuery = MatchAllQuery.of(m -> m)._toQuery();
        Query termQuery = TermQuery.of(t -> t.field("category").value(category))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(matchAllQuery)
                .filter(termQuery)
        )._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Đếm tổng số documents với MatchAll
    public long countAllArticles() {
        Query matchAllQuery = MatchAllQuery.of(m -> m)._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .build();

        return elasticsearchOperations.count(searchQuery, Article.class);
    }

    // Sử dụng repository method đơn giản cho MatchAll
    public List<Article> findAllArticlesUsingRepository() {
        return (List<Article>) articleRepository.findAll();
    }

    // Save article
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    // Save multiple articles
    public Iterable<Article> saveAllArticles(List<Article> articles) {
        return articleRepository.saveAll(articles);
    }

    // Delete all articles
    public void deleteAllArticles() {
        articleRepository.deleteAll();
    }

    // Tạo sample data để test
    public void createSampleData() {
        List<Article> sampleArticles = List.of(
                new Article("Spring Boot Tutorial", "Learn Spring Boot framework basics", "John Doe", "Programming", "spring,java,tutorial"),
                new Article("Elasticsearch Guide", "Complete guide to Elasticsearch", "Jane Smith", "Database", "elasticsearch,search,nosql"),
                new Article("React Development", "Modern React development practices", "Mike Johnson", "Frontend", "react,javascript,frontend"),
                new Article("Machine Learning Basics", "Introduction to ML concepts", "Sarah Wilson", "AI", "ml,python,data-science"),
                new Article("Docker Container Guide", "Containerization with Docker", "Tom Brown", "DevOps", "docker,containers,deployment")
        );

        saveAllArticles(sampleArticles);
    }
}
