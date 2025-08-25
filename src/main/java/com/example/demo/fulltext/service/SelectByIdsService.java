package com.example.demo.fulltext.service;

import com.example.demo.fulltext.model.DocumentEntity;
import com.example.demo.fulltext.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectByIdsService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    // SelectByIds query - Tìm documents theo danh sách IDs
    public List<DocumentEntity> findDocumentsByIds(List<String> ids) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(idsQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // SelectByIds với pagination
    public List<DocumentEntity> findDocumentsByIdsWithPagination(List<String> ids, int page, int size) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(idsQuery)
                .withPageable(pageable)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // SelectByIds kết hợp với filter theo type
    public List<DocumentEntity> findDocumentsByIdsAndType(List<String> ids, String type) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();
        Query termQuery = TermQuery.of(t -> t.field("type").value(type))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(idsQuery)
                .filter(termQuery)
        )._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // SelectByIds kết hợp với filter theo status
    public List<DocumentEntity> findDocumentsByIdsAndStatus(List<String> ids, String status) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();
        Query termQuery = TermQuery.of(t -> t.field("status").value(status))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(idsQuery)
                .filter(termQuery)
        )._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // SelectByIds với boost
    public List<DocumentEntity> findDocumentsByIdsWithBoost(List<String> ids, float boost) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i ->
            i.values(ids).boost(boost)
        )._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(idsQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // SelectByIds với multiple filters (type và status)
    public List<DocumentEntity> findDocumentsByIdsWithMultipleFilters(List<String> ids, String type, String status) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();
        Query typeQuery = TermQuery.of(t -> t.field("type").value(type))._toQuery();
        Query statusQuery = TermQuery.of(t -> t.field("status").value(status))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(idsQuery)
                .filter(typeQuery)
                .filter(statusQuery)
        )._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Sử dụng repository method đơn giản cho SelectByIds
    public List<DocumentEntity> findDocumentsByIdsUsingRepository(List<String> ids) {
        return (List<DocumentEntity>) documentRepository.findAllById(ids);
    }

    // Đếm số documents theo IDs
    public long countDocumentsByIds(List<String> ids) {
        Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.of(i -> i.values(ids))._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(idsQuery)
                .build();

        return elasticsearchOperations.count(searchQuery, DocumentEntity.class);
    }

    // Lấy danh sách tất cả IDs có trong index documents
    public List<String> getAllDocumentIds() {
        Query matchAllQuery = co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery.of(m -> m)._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .build();

        SearchHits<DocumentEntity> searchHits = elasticsearchOperations.search(searchQuery, DocumentEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getId)
                .collect(Collectors.toList());
    }

    // Kiểm tra IDs nào tồn tại trong index
    public List<String> checkExistingDocumentIds(List<String> ids) {
        List<DocumentEntity> foundDocuments = findDocumentsByIds(ids);
        return foundDocuments.stream()
                .map(DocumentEntity::getId)
                .collect(Collectors.toList());
    }

    // Lấy documents theo IDs và sắp xếp theo priority
    public List<DocumentEntity> findDocumentsByIdsOrderByPriority(List<String> ids) {
        List<DocumentEntity> documents = findDocumentsByIds(ids);
        return documents.stream()
                .sorted((d1, d2) -> Integer.compare(d2.getPriority(), d1.getPriority())) // Sắp xếp giảm dần
                .collect(Collectors.toList());
    }

    // Save document
    public DocumentEntity saveDocument(DocumentEntity document) {
        return documentRepository.save(document);
    }

    // Save multiple documents
    public Iterable<DocumentEntity> saveAllDocuments(List<DocumentEntity> documents) {
        return documentRepository.saveAll(documents);
    }

    // Delete all documents
    public void deleteAllDocuments() {
        documentRepository.deleteAll();
    }

    // Tạo sample data để test SelectByIds
    public void createDocumentSampleData() {
        List<DocumentEntity> sampleDocuments = List.of(
                new DocumentEntity("User Manual", "Complete user guide for the application", "MANUAL", "ACTIVE", "guide,user,help", 5),
                new DocumentEntity("API Documentation", "REST API reference documentation", "API_DOC", "ACTIVE", "api,rest,reference", 4),
                new DocumentEntity("Technical Specification", "System architecture and design", "SPEC", "DRAFT", "architecture,design,system", 3),
                new DocumentEntity("Release Notes", "Latest version release information", "RELEASE", "ACTIVE", "release,version,changelog", 2),
                new DocumentEntity("Configuration Guide", "System configuration and setup", "GUIDE", "ACTIVE", "config,setup,installation", 4),
                new DocumentEntity("Troubleshooting Guide", "Common issues and solutions", "GUIDE", "REVIEW", "troubleshoot,issues,solutions", 5),
                new DocumentEntity("Security Policy", "Security guidelines and procedures", "POLICY", "ACTIVE", "security,policy,guidelines", 5),
                new DocumentEntity("Development Guide", "Developer setup and coding standards", "GUIDE", "DRAFT", "development,coding,standards", 3)
        );

        saveAllDocuments(sampleDocuments);
    }
}
