package com.example.demo.fulltext.repository;

import com.example.demo.fulltext.model.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {

    // Range query methods cho các field số
    List<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductEntity> findByQuantityBetween(Integer minQuantity, Integer maxQuantity);
    List<ProductEntity> findByRatingBetween(Double minRating, Double maxRating);
    List<ProductEntity> findByViewCountBetween(Integer minViews, Integer maxViews);
    List<ProductEntity> findByWeightBetween(Double minWeight, Double maxWeight);
    List<ProductEntity> findByDiscountBetween(Integer minDiscount, Integer maxDiscount);

    // Range query methods cho date
    List<ProductEntity> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ProductEntity> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Greater than / Less than methods
    List<ProductEntity> findByPriceGreaterThan(Double price);
    List<ProductEntity> findByPriceLessThan(Double price);
    List<ProductEntity> findByQuantityGreaterThan(Integer quantity);
    List<ProductEntity> findByRatingGreaterThanEqual(Double rating);

    // Combination queries
    List<ProductEntity> findByCategory(String category);
    List<ProductEntity> findByBrand(String brand);
    List<ProductEntity> findByInStock(Boolean inStock);
    List<ProductEntity> findByCategoryAndPriceBetween(String category, Double minPrice, Double maxPrice);
}
