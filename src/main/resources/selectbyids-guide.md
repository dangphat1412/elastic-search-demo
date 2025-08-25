# SelectByIds Query - Hướng dẫn thực hành

## Tổng quan
SelectByIds query được thiết kế để tìm kiếm documents theo danh sách IDs cụ thể. Đây là một trong những query nhanh nhất trong Elasticsearch vì nó tìm kiếm trực tiếp theo document ID.

## Cấu trúc Package SelectByIds

### 1. Model: DocumentEntity.java
- Entity riêng cho SelectByIds với các field: title, description, type, status, createdDate, keywords, priority, score
- Sử dụng index "documents" để tách biệt với Article của MatchAll
- Có thêm field priority và score để demo sorting và filtering

### 2. Repository: DocumentRepository.java
- Interface kế thừa ElasticsearchRepository cho DocumentEntity
- Cung cấp các method query theo type, status, priority

### 3. Service: SelectByIdsService.java
- Service riêng chuyên cho SelectByIds operations:
  - `findDocumentsByIds()`: SelectByIds query cơ bản
  - `findDocumentsByIdsWithPagination()`: SelectByIds với phân trang
  - `findDocumentsByIdsAndType()`: SelectByIds + filter theo type
  - `findDocumentsByIdsAndStatus()`: SelectByIds + filter theo status
  - `findDocumentsByIdsWithBoost()`: SelectByIds với boost
  - `findDocumentsByIdsWithMultipleFilters()`: SelectByIds + multiple filters
  - `findDocumentsByIdsUsingRepository()`: Sử dụng repository method
  - `countDocumentsByIds()`: Đếm documents theo IDs
  - `getAllDocumentIds()`: Lấy tất cả IDs
  - `checkExistingDocumentIds()`: Kiểm tra IDs tồn tại
  - `findDocumentsByIdsOrderByPriority()`: SelectByIds + sorting
  - `createDocumentSampleData()`: Tạo dữ liệu mẫu

### 4. Controller: SelectByIdsController.java
- Controller riêng với endpoint prefix `/api/selectbyids`
- Cung cấp 16 endpoints khác nhau để test SelectByIds
- Bao gồm cả performance testing và batch processing

### 5. DTO: SelectByIdsResponse.java
- DTO riêng để handle response với metadata
- Chứa thông tin về execution time, missing IDs, query type

## Các API Endpoints

### Basic SelectByIds Endpoints

#### 1. SelectByIds cơ bản
```
POST /api/selectbyids/documents
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 2. SelectByIds với pagination
```
POST /api/selectbyids/documents/paginated?page=0&size=2
Content-Type: application/json

["doc1", "doc2", "doc3", "doc4"]
```

#### 3. SelectByIds với boost
```
POST /api/selectbyids/documents/boost?boost=2.0
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

### Filter Endpoints

#### 4. SelectByIds + filter theo type
```
POST /api/selectbyids/documents/type/GUIDE
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 5. SelectByIds + filter theo status
```
POST /api/selectbyids/documents/status/ACTIVE
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 6. SelectByIds + multiple filters
```
POST /api/selectbyids/documents/filters?type=GUIDE&status=ACTIVE
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

### Repository và Utility Endpoints

#### 7. SelectByIds sử dụng repository
```
POST /api/selectbyids/documents/repository
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 8. Đếm documents theo IDs
```
POST /api/selectbyids/documents/count
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 9. Lấy tất cả IDs
```
GET /api/selectbyids/documents/all-ids
```

#### 10. Kiểm tra IDs tồn tại
```
POST /api/selectbyids/documents/check-existing
Content-Type: application/json

["existing_doc", "non_existing_doc", "another_doc"]
```

#### 11. SelectByIds + sorting theo priority
```
POST /api/selectbyids/documents/ordered-by-priority
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

### Data Management Endpoints

#### 12. Tạo sample data
```
POST /api/selectbyids/sample-data
```

#### 13. Tạo document mới
```
POST /api/selectbyids/documents/create
Content-Type: application/json

{
    "title": "New Document",
    "description": "Document description",
    "type": "MANUAL",
    "status": "ACTIVE",
    "keywords": "new,document",
    "priority": 3
}
```

#### 14. Xóa tất cả documents
```
DELETE /api/selectbyids/documents
```

### Advanced Endpoints

#### 15. Batch processing
```
POST /api/selectbyids/documents/batch-process
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

#### 16. Performance test
```
POST /api/selectbyids/documents/performance-test
Content-Type: application/json

["doc1", "doc2", "doc3"]
```

## Cách sử dụng để thực hành

### Bước 1: Tạo dữ liệu mẫu cho DocumentEntity
```bash
curl -X POST http://localhost:8080/api/selectbyids/sample-data
```

### Bước 2: Lấy danh sách IDs để test
```bash
curl -X GET http://localhost:8080/api/selectbyids/documents/all-ids
```

### Bước 3: Test SelectByIds cơ bản
```bash
curl -X POST http://localhost:8080/api/selectbyids/documents \
  -H "Content-Type: application/json" \
  -d '["id1", "id2"]'
```

### Bước 4: Test SelectByIds với filter
```bash
curl -X POST http://localhost:8080/api/selectbyids/documents/type/GUIDE \
  -H "Content-Type: application/json" \
  -d '["id1", "id2", "id3"]'
```

### Bước 5: Test performance comparison
```bash
curl -X POST http://localhost:8080/api/selectbyids/documents/performance-test \
  -H "Content-Type: application/json" \
  -d '["id1", "id2", "id3"]'
```

## Các ví dụ Query trong code

### 1. SelectByIds cơ bản
```java
Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery
    .of(i -> i.values(ids))._toQuery();

NativeQuery searchQuery = NativeQuery.builder()
    .withQuery(idsQuery)
    .build();
```

### 2. SelectByIds với Bool Query và Multiple Filters
```java
Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery
    .of(i -> i.values(ids))._toQuery();
Query typeQuery = TermQuery.of(t -> t.field("type").value(type))._toQuery();
Query statusQuery = TermQuery.of(t -> t.field("status").value(status))._toQuery();

Query boolQuery = BoolQuery.of(b -> b
    .must(idsQuery)
    .filter(typeQuery)
    .filter(statusQuery)
)._toQuery();
```

### 3. SelectByIds với Pagination
```java
Query idsQuery = co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery
    .of(i -> i.values(ids))._toQuery();
Pageable pageable = PageRequest.of(page, size);

NativeQuery searchQuery = NativeQuery.builder()
    .withQuery(idsQuery)
    .withPageable(pageable)
    .build();
```

## So sánh với MatchAll Query

| Aspect | MatchAll Query | SelectByIds Query |
|--------|----------------|-------------------|
| **Index** | articles | documents |
| **Model** | Article | DocumentEntity |
| **Controller Path** | /api/fulltext/* | /api/selectbyids/* |
| **Use Case** | Lấy tất cả documents | Lấy documents theo IDs cụ thể |
| **Performance** | Chậm với dataset lớn | Rất nhanh, O(1) lookup |
| **Memory Usage** | Cao nếu không phân trang | Thấp, chỉ load cần thiết |
| **Flexibility** | Rất linh hoạt với filters | Hạn chế nhưng rất hiệu quả |

## Document Entity Fields

```json
{
  "id": "auto-generated",
  "title": "Document title",
  "description": "Document description",
  "type": "MANUAL|API_DOC|SPEC|RELEASE|GUIDE|POLICY",
  "status": "ACTIVE|DRAFT|REVIEW|ARCHIVED",
  "createdDate": "2025-08-25T15:30:00.000",
  "keywords": "comma,separated,keywords",
  "priority": 1-5,
  "score": 0.0-10.0
}
```

## Sample Data được tạo
- 8 documents với các type và status khác nhau
- Priority từ 2-5
- Các type: MANUAL, API_DOC, SPEC, RELEASE, GUIDE, POLICY
- Các status: ACTIVE, DRAFT, REVIEW

## Best Practices cho SelectByIds

### Performance Tips
1. **Batch Size**: Giới hạn 100-500 IDs per request
2. **Caching**: Cache kết quả cho IDs thường truy cập
3. **Pagination**: Sử dụng pagination khi có nhiều IDs
4. **Filtering**: Apply filters để giảm kết quả trả về

### Error Handling
1. **Missing IDs**: IDs không tồn tại sẽ bị bỏ qua
2. **Validation**: Validate IDs format trước khi query
3. **Timeout**: Set reasonable timeout cho large batch

### Use Cases thực tế
1. **User Favorites**: Lấy danh sách documents yêu thích
2. **Bulk Operations**: Xử lý batch documents
3. **Related Documents**: Lấy documents liên quan
4. **Cache Warm-up**: Pre-load documents vào cache
5. **Data Migration**: Di chuyển specific documents
