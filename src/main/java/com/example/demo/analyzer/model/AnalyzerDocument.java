package com.example.demo.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "analyzer_demo")
@Setting(settingPath = "analyzer-settings.json")
public class AnalyzerDocument {

    @Id
    private String id;

    // Standard analyzer (default)
    @Field(type = FieldType.Text, analyzer = "standard")
    private String standardText;

    // Keyword analyzer - không phân tích, giữ nguyên
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String keywordText;

    // Simple analyzer - chỉ lowercase và split bằng non-letter
    @Field(type = FieldType.Text, analyzer = "simple")
    private String simpleText;

    // Whitespace analyzer - chỉ split bằng whitespace
    @Field(type = FieldType.Text, analyzer = "whitespace")
    private String whitespaceText;

    // Stop analyzer - loại bỏ stop words
    @Field(type = FieldType.Text, analyzer = "stop")
    private String stopText;

    // Language analyzer - Vietnamese
    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    private String vietnameseText;

    // Custom analyzer
    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String customText;

    // Autocomplete field với edge n-gram
    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String autocompleteText;

    // Email analyzer
    @Field(type = FieldType.Text, analyzer = "email_analyzer")
    private String emailText;

    // Multi-field example
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private String category;

    private String description;
}
