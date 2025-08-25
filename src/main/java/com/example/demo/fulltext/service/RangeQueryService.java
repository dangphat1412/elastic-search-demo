package com.example.demo.fulltext.service;

import com.example.demo.fulltext.model.ProductEntity;
import com.example.demo.fulltext.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RangeQueryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    // ===== NUMERIC RANGE QUERIES USING CRITERIA QUERY =====

    // Range query cho price sử dụng Criteria (native approach)
    public List<ProductEntity> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        Criteria criteria = new Criteria("price").between(minPrice, maxPrice);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Range query cho quantity sử dụng Criteria
    public List<ProductEntity> findProductsByQuantityRange(Integer minQuantity, Integer maxQuantity) {
        Criteria criteria = new Criteria("quantity").between(minQuantity, maxQuantity);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Range query cho rating sử dụng Criteria
    public List<ProductEntity> findProductsByRatingRange(Double minRating, Double maxRating) {
        Criteria criteria = new Criteria("rating").between(minRating, maxRating);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Range query cho weight sử dụng Criteria
    public List<ProductEntity> findProductsByWeightRange(Double minWeight, Double maxWeight) {
        Criteria criteria = new Criteria("weight").between(minWeight, maxWeight);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== DATE RANGE QUERIES USING CRITERIA =====

    // Range query cho created date sử dụng Criteria
    public List<ProductEntity> findProductsByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = new Criteria("createdDate").between(startDate, endDate);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Range query cho last updated sử dụng Criteria
    public List<ProductEntity> findProductsByLastUpdatedRange(LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = new Criteria("lastUpdated").between(startDate, endDate);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== GREATER THAN / LESS THAN QUERIES USING CRITERIA =====

    // Greater than query cho price sử dụng Criteria
    public List<ProductEntity> findProductsPriceGreaterThan(Double price) {
        Criteria criteria = new Criteria("price").greaterThan(price);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Less than query cho price sử dụng Criteria
    public List<ProductEntity> findProductsPriceLessThan(Double price) {
        Criteria criteria = new Criteria("price").lessThan(price);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Greater than equal cho rating sử dụng Criteria
    public List<ProductEntity> findProductsRatingGreaterThanEqual(Double rating) {
        Criteria criteria = new Criteria("rating").greaterThanEqual(rating);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== COMBINATION QUERIES USING CRITERIA AND NATIVE QUERY =====

    // Range query kết hợp với category filter sử dụng Criteria
    public List<ProductEntity> findProductsByPriceRangeAndCategory(Double minPrice, Double maxPrice, String category) {
        Criteria priceCriteria = new Criteria("price").between(minPrice, maxPrice);
        Criteria categoryCriteria = new Criteria("category").is(category);
        Criteria combinedCriteria = priceCriteria.and(categoryCriteria);

        CriteriaQuery query = new CriteriaQuery(combinedCriteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // Multiple range queries sử dụng Criteria
    public List<ProductEntity> findProductsByMultipleRanges(Double minPrice, Double maxPrice,
                                                           Integer minQuantity, Integer maxQuantity) {
        Criteria priceCriteria = new Criteria("price").between(minPrice, maxPrice);
        Criteria quantityCriteria = new Criteria("quantity").between(minQuantity, maxQuantity);
        Criteria combinedCriteria = priceCriteria.and(quantityCriteria);

        CriteriaQuery query = new CriteriaQuery(combinedCriteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== PAGINATION SUPPORT USING CRITERIA =====

    // Range query với pagination sử dụng Criteria
    public List<ProductEntity> findProductsByPriceRangeWithPagination(Double minPrice, Double maxPrice,
                                                                      int page, int size) {
        Criteria criteria = new Criteria("price").between(minPrice, maxPrice);
        CriteriaQuery query = new CriteriaQuery(criteria);

        Pageable pageable = PageRequest.of(page, size);
        query.setPageable(pageable);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== REPOSITORY METHODS (for comparison) =====

    // Sử dụng repository method cho range query
    public List<ProductEntity> findProductsByPriceRangeUsingRepository(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<ProductEntity> findProductsByQuantityRangeUsingRepository(Integer minQuantity, Integer maxQuantity) {
        return productRepository.findByQuantityBetween(minQuantity, maxQuantity);
    }

    public List<ProductEntity> findProductsByRatingRangeUsingRepository(Double minRating, Double maxRating) {
        return productRepository.findByRatingBetween(minRating, maxRating);
    }

    // ===== COUNT OPERATIONS USING CRITERIA =====

    // Đếm products trong price range sử dụng Criteria
    public long countProductsByPriceRange(Double minPrice, Double maxPrice) {
        Criteria criteria = new Criteria("price").between(minPrice, maxPrice);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations.count(query, ProductEntity.class);
    }

    // Đếm products trong quantity range sử dụng Criteria
    public long countProductsByQuantityRange(Integer minQuantity, Integer maxQuantity) {
        Criteria criteria = new Criteria("quantity").between(minQuantity, maxQuantity);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations.count(query, ProductEntity.class);
    }

    // Đếm products trong rating range sử dụng Criteria
    public long countProductsByRatingRange(Double minRating, Double maxRating) {
        Criteria criteria = new Criteria("rating").between(minRating, maxRating);
        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations.count(query, ProductEntity.class);
    }

    // ===== ADVANCED NATIVE QUERIES USING BOOL QUERY =====

    // Range query với boost sử dụng MatchAll + Bool Query
    public List<ProductEntity> findProductsByPriceRangeWithBoost(Double minPrice, Double maxPrice, float boost) {
        // Sử dụng MatchAll query với boost và filter để tạo range effect
        Query matchAllQuery = MatchAllQuery.of(m -> m.boost(boost))._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(matchAllQuery)
                .build();

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(searchQuery, ProductEntity.class);

        // Filter kết quả theo price range (post-processing)
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Complex bool query với multiple conditions
    public List<ProductEntity> findProductsWithComplexConditions(Double minPrice, Double maxPrice,
                                                                String category, Double minRating) {
        Criteria priceCriteria = new Criteria("price").between(minPrice, maxPrice);
        Criteria categoryCriteria = new Criteria("category").is(category);
        Criteria ratingCriteria = new Criteria("rating").greaterThanEqual(minRating);

        Criteria combinedCriteria = priceCriteria.and(categoryCriteria).and(ratingCriteria);
        CriteriaQuery query = new CriteriaQuery(combinedCriteria);

        SearchHits<ProductEntity> searchHits = elasticsearchOperations.search(query, ProductEntity.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ===== UTILITY METHODS =====

    // Save product
    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    // Save multiple products
    public Iterable<ProductEntity> saveAllProducts(List<ProductEntity> products) {
        return productRepository.saveAll(products);
    }

    // Delete all products
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    // Get all products (for getting all IDs)
    public List<ProductEntity> findAllProducts() {
        return (List<ProductEntity>) productRepository.findAll();
    }

    // Tạo sample data cho ProductEntity
    public void createProductSampleData() {
        List<ProductEntity> sampleProducts = List.of(
                new ProductEntity("iPhone 15", "Latest Apple smartphone", "Electronics", "Apple", 999.99, 50, 4.5, 0.2, 10),
                new ProductEntity("MacBook Pro", "Professional laptop", "Electronics", "Apple", 2499.99, 20, 4.8, 2.1, 5),
                new ProductEntity("Samsung Galaxy S24", "Android flagship phone", "Electronics", "Samsung", 899.99, 75, 4.3, 0.19, 15),
                new ProductEntity("Nike Air Max", "Running shoes", "Footwear", "Nike", 129.99, 100, 4.2, 0.8, 20),
                new ProductEntity("Adidas Ultraboost", "Athletic shoes", "Footwear", "Adidas", 179.99, 80, 4.6, 0.9, 25),
                new ProductEntity("Sony WH-1000XM5", "Noise cancelling headphones", "Electronics", "Sony", 399.99, 30, 4.7, 0.3, 12),
                new ProductEntity("Dell XPS 13", "Ultrabook laptop", "Electronics", "Dell", 1299.99, 40, 4.4, 1.2, 8),
                new ProductEntity("Canon EOS R5", "Mirrorless camera", "Electronics", "Canon", 3899.99, 15, 4.9, 0.65, 3),
                new ProductEntity("Levi's 501 Jeans", "Classic denim jeans", "Clothing", "Levi's", 79.99, 200, 4.1, 0.6, 30),
                new ProductEntity("The North Face Jacket", "Outdoor jacket", "Clothing", "The North Face", 249.99, 60, 4.5, 1.1, 18)
        );

        saveAllProducts(sampleProducts);
    }
}
