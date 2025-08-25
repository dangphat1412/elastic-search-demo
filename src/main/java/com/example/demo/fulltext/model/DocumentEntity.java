package com.example.demo.fulltext.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.DateFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "documents")
public class DocumentEntity {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdDate;

    @Field(type = FieldType.Text)
    private String keywords;

    @Field(type = FieldType.Integer)
    private Integer priority;

    @Field(type = FieldType.Double)
    private Double score;

    // Custom constructor without id
    public DocumentEntity(String title, String description, String type, String status, String keywords, Integer priority) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.keywords = keywords;
        this.priority = priority;
        this.createdDate = LocalDateTime.now();
        this.score = 0.0;
    }
}
