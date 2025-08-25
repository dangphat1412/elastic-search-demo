# Range Query - Hướng dẫn thực hành

## Tổng quan
Range Query được sử dụng để tìm kiếm documents theo khoảng giá trị (range). Đây là một trong những query phổ biến nhất trong Elasticsearch, đặc biệt hữu ích cho:
- Tìm kiếm theo giá (price range)
- Lọc theo ngày tháng (date range)
- Tìm kiếm theo số lượng, rating, weight, v.v.

## Cấu trúc Package Range Query

### 1. Model: ProductEntity.java
- Entity chuyên cho Range Query với index "products"
- Các field số: price, quantity, rating, viewCount, weight, discount
- Các field ngày: createdDate, lastUpdated
- Các field boolean và text: inStock, category, brand, name, description, tags

### 2. Repository: ProductRepository.java
- Interface kế thừa ElasticsearchRepository cho ProductEntity
- Cung cấp các method range query: findByPriceBetween, findByQuantityBetween, v.v.
- Hỗ trợ các comparison operators: GreaterThan, LessThan, GreaterThanEqual

### 3. Service: RangeQueryService.java
- Service chuyên biệt cho Range Query operations:
  - **Numeric Range Methods**: price, quantity, rating, weight
  - **Date Range Methods**: createdDate, lastUpdated
  - **Comparison Methods**: greater than, less than, greater than equal
  - **Combination Methods**: range + filters, multiple ranges
  - **Pagination Support**: range query với phân trang
  - **Repository Methods**: sử dụng Spring Data methods
  - **Count Operations**: đếm documents trong range
  - **Utility Methods**: create sample data, save/delete

### 4. Controller: RangeQueryController.java
- Controller riêng với endpoint prefix `/api/range`
- 20+ endpoints khác nhau để test Range Query
- Bao gồm cả demo endpoints cho e-commerce, inventory management, rating analysis

### 5. DTO: RangeQueryResponse.java
- DTO riêng cho Range Query response
- Chứa metadata về range field, min/max values, statistics

## Các API Endpoints

### Numeric Range Endpoints

#### 1. Price Range Query
```
GET /api/range/products/price?minPrice=100&maxPrice=500
```

#### 2. Quantity Range Query
```
GET /api/range/products/quantity?minQuantity=10&maxQuantity=100
```

#### 3. Rating Range Query
```
GET /api/range/products/rating?minRating=4.0&maxRating=5.0
```

#### 4. Weight Range Query
```
GET /api/range/products/weight?minWeight=0.5&maxWeight=2.0
```

### Date Range Endpoints

#### 5. Created Date Range
```
GET /api/range/products/created-date?startDate=2025-01-01T00:00:00&endDate=2025-12-31T23:59:59
```

#### 6. Last Updated Range
```
GET /api/range/products/last-updated?startDate=2025-08-01T00:00:00&endDate=2025-08-31T23:59:59
```

### Comparison Endpoints

#### 7. Price Greater Than
```
GET /api/range/products/price/greater-than?price=1000
```

#### 8. Price Less Than
```
GET /api/range/products/price/less-than?price=200
```

#### 9. Rating Greater Than Equal
```
GET /api/range/products/rating/greater-than-equal?rating=4.5
```

### Combination Endpoints

#### 10. Price Range + Category Filter
```
GET /api/range/products/price-category?minPrice=100&maxPrice=500&category=Electronics
```

#### 11. Multiple Range Queries
```
GET /api/range/products/multiple-ranges?minPrice=100&maxPrice=1000&minQuantity=10&maxQuantity=100
```

### Pagination Endpoints

#### 12. Price Range với Pagination
```
GET /api/range/products/price/paginated?minPrice=100&maxPrice=500&page=0&size=5
```

### Repository Endpoints

#### 13. Price Range sử dụng Repository
```
GET /api/range/products/price/repository?minPrice=100&maxPrice=500
```

#### 14. Quantity Range sử dụng Repository
```
GET /api/range/products/quantity/repository?minQuantity=10&maxQuantity=100
```

#### 15. Rating Range sử dụng Repository
```
GET /api/range/products/rating/repository?minRating=4.0&maxRating=5.0
```

### Count Endpoints

#### 16. Count Products trong Price Range
```
GET /api/range/products/price/count?minPrice=100&maxPrice=500
```

### Data Management Endpoints

#### 17. Tạo Sample Data
```
POST /api/range/sample-data
```

#### 18. Tạo Product mới
```
POST /api/range/products/create
Content-Type: application/json

{
    "name": "New Product",
    "description": "Product description",
    "category": "Electronics",
    "brand": "Apple",
    "price": 999.99,
    "quantity": 50,
    "rating": 4.5,
    "weight": 0.5,
    "discount": 10
}
```

#### 19. Xóa tất cả Products
```
DELETE /api/range/products
```

### Advanced Demo Endpoints

#### 20. Price Filter Demo (E-commerce)
```
GET /api/range/products/price-filter?budget=500
GET /api/range/products/price-filter?priceCategory=mid-range
```

#### 21. Inventory Status Demo
```
GET /api/range/products/inventory-status
```

#### 22. Rating Analysis Demo
```
GET /api/range/products/rating-analysis
```

## Cách sử dụng để thực hành

### Bước 1: Tạo dữ liệu mẫu cho ProductEntity
```bash
curl -X POST http://localhost:8080/api/range/sample-data
```

### Bước 2: Test Price Range Query
```bash
curl -X GET "http://localhost:8080/api/range/products/price?minPrice=100&maxPrice=500"
```

### Bước 3: Test Rating Range Query
```bash
curl -X GET "http://localhost:8080/api/range/products/rating?minRating=4.0&maxRating=5.0"
```

### Bước 4: Test Combination Query (Price + Category)
```bash
curl -X GET "http://localhost:8080/api/range/products/price-category?minPrice=100&maxPrice=1000&category=Electronics"
```

### Bước 5: Test Advanced Demo - Inventory Status
```bash
curl -X GET http://localhost:8080/api/range/products/inventory-status
```

## Các ví dụ Query trong code

### 1. Basic Range Query
```java
Query rangeQuery = RangeQuery.of(r -> r
    .field("price")
    .gte(JsonData.of(minPrice))
    .lte(JsonData.of(maxPrice))
)._toQuery();
```

### 2. Greater Than Query
```java
Query rangeQuery = RangeQuery.of(r -> r
    .field("price")
    .gt(JsonData.of(price))
)._toQuery();
```

### 3. Date Range Query
```java
Query rangeQuery = RangeQuery.of(r -> r
    .field("createdDate")
    .gte(JsonData.of(startDate.toString()))
    .lte(JsonData.of(endDate.toString()))
)._toQuery();
```

### 4. Range Query với Bool Query
```java
Query rangeQuery = RangeQuery.of(r -> r
    .field("price")
    .gte(JsonData.of(minPrice))
    .lte(JsonData.of(maxPrice))
)._toQuery();

Query termQuery = TermQuery.of(t -> t.field("category").value(category))._toQuery();

Query boolQuery = BoolQuery.of(b -> b
    .must(rangeQuery)
    .filter(termQuery)
)._toQuery();
```

### 5. Multiple Range Queries
```java
Query priceRangeQuery = RangeQuery.of(r -> r
    .field("price")
    .gte(JsonData.of(minPrice))
    .lte(JsonData.of(maxPrice))
)._toQuery();

Query quantityRangeQuery = RangeQuery.of(r -> r
    .field("quantity")
    .gte(JsonData.of(minQuantity))
    .lte(JsonData.of(maxQuantity))
)._toQuery();

Query boolQuery = BoolQuery.of(b -> b
    .must(priceRangeQuery)
    .must(quantityRangeQuery)
)._toQuery();
```

## So sánh với các Query khác

| Aspect | MatchAll Query | SelectByIds Query | Range Query |
|--------|----------------|-------------------|-------------|
| **Index** | articles | documents | products |
| **Model** | Article | DocumentEntity | ProductEntity |
| **Controller Path** | /api/fulltext/* | /api/selectbyids/* | /api/range/* |
| **Use Case** | Lấy tất cả documents | Lấy theo IDs cụ thể | Lấy theo khoảng giá trị |
| **Performance** | Chậm với dataset lớn | Rất nhanh, O(1) | Trung bình, phụ thuộc range |
| **Flexibility** | Rất linh hoạt | Hạn chế | Rất linh hoạt cho numeric/date |
| **Best Use Cases** | Listing, debugging | Favorites, bulk ops | Filtering, e-commerce |

## Range Query Operators

| Operator | Ý nghĩa | Ví dụ |
|----------|---------|-------|
| **gte** | Greater Than Equal (≥) | `price ≥ 100` |
| **gt** | Greater Than (>) | `price > 100` |
| **lte** | Less Than Equal (≤) | `price ≤ 500` |
| **lt** | Less Than (<) | `price < 500` |

## ProductEntity Fields & Sample Data

### Fields:
```json
{
  "id": "auto-generated",
  "name": "Product name",
  "description": "Product description", 
  "category": "Electronics|Footwear|Clothing",
  "brand": "Apple|Samsung|Nike|Dell|etc",
  "price": 79.99-3899.99,
  "quantity": 15-200,
  "rating": 4.1-4.9,
  "viewCount": 0,
  "createdDate": "2025-08-25T22:xx:xx",
  "lastUpdated": "2025-08-25T22:xx:xx",
  "weight": 0.19-2.1,
  "discount": 3-30,
  "inStock": true/false,
  "tags": ""
}
```

### Sample Products:
- iPhone 15 ($999.99, Electronics, Apple)
- MacBook Pro ($2499.99, Electronics, Apple)
- Nike Air Max ($129.99, Footwear, Nike)
- Canon EOS R5 ($3899.99, Electronics, Canon)
- Levi's 501 Jeans ($79.99, Clothing, Levi's)

## Use Cases thực tế

### 1. E-commerce Price Filtering
```bash
# Products under $200 (budget category)
curl -X GET "http://localhost:8080/api/range/products/price-filter?priceCategory=budget"

# Products in mid-range ($100-500)
curl -X GET "http://localhost:8080/api/range/products/price-filter?priceCategory=mid-range"
```

### 2. Inventory Management
```bash
# Check inventory status across all products
curl -X GET http://localhost:8080/api/range/products/inventory-status
```

### 3. Product Rating Analysis
```bash
# Analyze product ratings distribution
curl -X GET http://localhost:8080/api/range/products/rating-analysis
```

### 4. Date-based Filtering
```bash
# Products created this month
curl -X GET "http://localhost:8080/api/range/products/created-date?startDate=2025-08-01T00:00:00&endDate=2025-08-31T23:59:59"
```

## Best Practices cho Range Query

### Performance Tips
1. **Index Optimization**: Đảm bảo các numeric/date fields được index đúng
2. **Range Size**: Tránh range quá rộng, sử dụng pagination
3. **Caching**: Cache kết quả cho các range query phổ biến
4. **Filtering**: Kết hợp với filters để giảm dataset

### Query Design
1. **Inclusive vs Exclusive**: Sử dụng gte/lte cho inclusive, gt/lt cho exclusive
2. **Data Types**: Đảm bảo data types match (Double, Integer, LocalDateTime)
3. **Null Handling**: Xử lý null values trong range queries
4. **Validation**: Validate min <= max trước khi query

### Error Handling
1. **Invalid Ranges**: Xử lý trường hợp minValue > maxValue
2. **Data Type Mismatch**: Validate input types
3. **Null Values**: Handle null input parameters
4. **Empty Results**: Provide meaningful messages cho empty results

## Advanced Features

### 1. Statistical Analysis
Range Query có thể kết hợp với aggregations để:
- Tính average, min, max trong range
- Histogram distribution
- Percentile analysis

### 2. Dynamic Range Building
- Build range từ user preferences
- Auto-suggest ranges based on data distribution
- Smart range recommendation

### 3. Multi-field Range Queries
- Combine multiple range conditions
- Complex business logic với bool queries
- Priority-based filtering
