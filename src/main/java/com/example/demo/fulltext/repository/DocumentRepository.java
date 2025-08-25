package com.example.demo.fulltext.repository;

import com.example.demo.fulltext.model.DocumentEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends ElasticsearchRepository<DocumentEntity, String> {

    // Các method query cơ bản cho DocumentEntity
    List<DocumentEntity> findByType(String type);
    List<DocumentEntity> findByStatus(String status);
    List<DocumentEntity> findByTitleContaining(String title);
    List<DocumentEntity> findByDescriptionContaining(String description);
    List<DocumentEntity> findByPriorityGreaterThan(Integer priority);
    List<DocumentEntity> findByScoreGreaterThan(Double score);

    // Có thể thêm các custom query method khác nếu cần
}
