package com.example.demo.bulk.repository;

import com.example.demo.bulk.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByActiveTrue();

    List<Product> findByActiveFalse();

    List<Product> findByNameContainingIgnoreCase(String name);
}

