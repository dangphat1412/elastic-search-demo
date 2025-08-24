package com.example.demo.bulk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {
    "com.example.demo.bulk.repository",
    "com.example.demo.analyzer.repository"
})
public class ElasticsearchConfig {

}
