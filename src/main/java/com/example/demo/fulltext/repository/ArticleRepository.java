package com.example.demo.fulltext.repository;

import com.example.demo.fulltext.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    // Các method query cơ bản
    List<Article> findByAuthor(String author);
    List<Article> findByCategory(String category);
    List<Article> findByTitleContaining(String title);
    List<Article> findByContentContaining(String content);

    // Có thể thêm các custom query method khác nếu cần
}
