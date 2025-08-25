# Full-Text Search với MatchAll Query - Hướng dẫn thực hành

## Tổng quan
Package `fulltext` được tạo để thực hành sử dụng Spring Data Elasticsearch với MatchAll query. MatchAll query là một trong những query đơn giản nhất trong Elasticsearch, nó trả về tất cả documents trong index.

## Cấu trúc Package

### 1. Model: Article.java
- Định nghĩa entity Article với các field như title, content, author, category, publishedDate, tags, views
- Sử dụng các annotation Elasticsearch để mapping với index "articles"

### 2. Repository: ArticleRepository.java
- Interface kế thừa ElasticsearchRepository
- Cung cấp các method cơ bản để tương tác với Elasticsearch

### 3. Service: FullTextSearchService.java
- Chứa các method thực hiện MatchAll query trong nhiều trường hợp khác nhau:
  - `findAllArticles()`: MatchAll query cơ bản
  - `findAllArticlesWithPagination()`: MatchAll với phân trang
  - `findAllArticlesWithBoost()`: MatchAll với boost score
  - `findAllArticlesByCategory()`: MatchAll kết hợp filter
  - `countAllArticles()`: Đếm tổng số documents
  - `createSampleData()`: Tạo dữ liệu mẫu để test

### 4. Controller: FullTextSearchController.java
- Expose các REST API endpoints để test các MatchAll query

## Các API Endpoints

### 1. Tạo dữ liệu mẫu
```
POST /api/fulltext/sample-data
```

### 2. MatchAll Query cơ bản
```
GET /api/fulltext/match-all
```

### 3. MatchAll với phân trang
```
GET /api/fulltext/match-all/paginated?page=0&size=5
```

### 4. MatchAll với boost
```
GET /api/fulltext/match-all/boost?boost=2.0
```

### 5. MatchAll với filter theo category
```
GET /api/fulltext/match-all/category/Programming
```

### 6. Đếm tổng số documents
```
GET /api/fulltext/count
```

### 7. Thêm article mới
```
POST /api/fulltext/articles
Content-Type: application/json

{
    "title": "New Article",
    "content": "Article content here",
    "author": "Author Name",
    "category": "Technology",
    "tags": "tech,programming"
}
```

### 8. Xóa tất cả articles
```
DELETE /api/fulltext/articles
```

## Cách sử dụng để thực hành

1. **Khởi động ứng dụng**: Đảm bảo Elasticsearch đang chạy và ứng dụng Spring Boot được start

2. **Tạo dữ liệu mẫu**: Gọi API tạo sample data trước
   ```bash
   curl -X POST http://localhost:8080/api/fulltext/sample-data
   ```

3. **Test MatchAll query cơ bản**:
   ```bash
   curl -X GET http://localhost:8080/api/fulltext/match-all
   ```

4. **Test MatchAll với phân trang**:
   ```bash
   curl -X GET "http://localhost:8080/api/fulltext/match-all/paginated?page=0&size=2"
   ```

5. **Test MatchAll với filter**:
   ```bash
   curl -X GET http://localhost:8080/api/fulltext/match-all/category/Programming
   ```

## Các ví dụ MatchAll Query trong code

### 1. MatchAll Query cơ bản
```java
NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.matchAllQuery())
    .build();
```

### 2. MatchAll với Pagination
```java
Pageable pageable = PageRequest.of(page, size);
NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.matchAllQuery())
    .withPageable(pageable)
    .build();
```

### 3. MatchAll với Boost
```java
NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.matchAllQuery().boost(boost))
    .build();
```

### 4. MatchAll kết hợp Filter
```java
NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.boolQuery()
        .must(QueryBuilders.matchAllQuery())
        .filter(QueryBuilders.termQuery("category", category)))
    .build();
```

## Lưu ý quan trọng

1. **MatchAll Query** trả về tất cả documents, thường được sử dụng khi:
   - Muốn lấy toàn bộ dữ liệu
   - Kết hợp với filter để lọc dữ liệu
   - Làm base query cho các aggregation
   - Test và debug

2. **Performance**: Với dataset lớn, nên sử dụng pagination để tránh memory issues

3. **Score**: MatchAll query mặc định có score = 1.0 cho tất cả documents, có thể sử dụng boost để thay đổi

4. **Best Practices**:
   - Luôn sử dụng pagination khi có thể
   - Kết hợp với filter thay vì must query khi có thể để tăng performance
   - Sử dụng count query để kiểm tra số lượng trước khi fetch data

