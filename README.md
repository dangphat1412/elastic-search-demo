# Elasticsearch Bulk Operations API

This Spring Boot application provides comprehensive Elasticsearch bulk operation APIs for practicing bulk operations.

## Available APIs

### 1. Bulk Index (Insert/Replace)
**POST** `/api/bulk/index`
```json
[
  {
    "id": "doc1",
    "title": "Sample Document",
    "content": "This is sample content",
    "category": "Technology",
    "status": "PUBLISHED",
    "priority": 1,
    "author": "John Doe",
    "tags": "important"
  }
]
```

### 2. Bulk Create (Fail if exists)
**POST** `/api/bulk/create`
```json
[
  {
    "title": "New Document",
    "content": "Content for new document",
    "category": "Business",
    "status": "DRAFT",
    "priority": 2,
    "author": "Jane Smith",
    "tags": "new"
  }
]
```

### 3. Bulk Update (Only existing documents)
**POST** `/api/bulk/update`
```json
[
  {
    "id": "existing-doc-id",
    "title": "Updated Title",
    "content": "Updated content",
    "status": "PUBLISHED"
  }
]
```

### 4. Bulk Upsert (Update or Create)
**POST** `/api/bulk/upsert`
```json
[
  {
    "id": "doc-to-upsert",
    "title": "Upserted Document",
    "content": "This will be created or updated",
    "category": "Science",
    "status": "PENDING",
    "priority": 3,
    "author": "Bob Johnson",
    "tags": "upsert"
  }
]
```

### 5. Bulk Delete
**DELETE** `/api/bulk/delete`
```json
["doc-id-1", "doc-id-2", "doc-id-3"]
```

### 6. Mixed Bulk Operations
**POST** `/api/bulk/mixed`
```json
[
  {
    "operation": "index",
    "id": "doc1",
    "document": {
      "title": "Indexed Document",
      "content": "Content for indexed doc",
      "category": "Technology"
    }
  },
  {
    "operation": "create",
    "document": {
      "title": "Created Document",
      "content": "Content for created doc",
      "category": "Business"
    }
  },
  {
    "operation": "update",
    "id": "existing-doc-id",
    "document": {
      "title": "Updated Document"
    }
  },
  {
    "operation": "delete",
    "id": "doc-to-delete"
  }
]
```

### 7. Legacy Bulk Operation
**POST** `/api/bulk/operation`
```json
{
  "operation": "CREATE",
  "documents": [
    {
      "title": "Document via legacy API",
      "content": "Content",
      "category": "Education"
    }
  ]
}
```

## Test Data APIs

### Generate Test Data
**POST** `/api/test/generate/{count}`
Generates sample documents for testing.

### Get All Documents
**GET** `/api/test/documents`

### Get Documents by Category
**GET** `/api/test/documents/category/{category}`

### Delete All Documents
**DELETE** `/api/test/documents/all`

### Index Management
- **POST** `/api/test/index/create` - Create index
- **DELETE** `/api/test/index/delete` - Delete index
- **GET** `/api/test/index/info` - Get index info

## Testing Examples

### 1. Setup
```bash
# Create index
curl -X POST http://localhost:8080/api/test/index/create

# Generate 10 test documents
curl -X POST http://localhost:8080/api/test/generate/10
```

### 2. Test Bulk Index
```bash
curl -X POST http://localhost:8080/api/bulk/index \
  -H "Content-Type: application/json" \
  -d '[
    {
      "title": "Bulk Test 1",
      "content": "Content for bulk test 1",
      "category": "Technology",
      "status": "PUBLISHED",
      "priority": 1,
      "author": "Test Author",
      "tags": "test"
    },
    {
      "title": "Bulk Test 2", 
      "content": "Content for bulk test 2",
      "category": "Business",
      "status": "DRAFT",
      "priority": 2,
      "author": "Test Author 2",
      "tags": "test"
    }
  ]'
```

### 3. Test Mixed Operations
```bash
curl -X POST http://localhost:8080/api/bulk/mixed \
  -H "Content-Type: application/json" \
  -d '[
    {
      "operation": "create",
      "document": {
        "title": "Created via Mixed",
        "content": "This is a created document",
        "category": "Mixed Test"
      }
    },
    {
      "operation": "index",
      "id": "mixed-test-1",
      "document": {
        "title": "Indexed via Mixed",
        "content": "This is an indexed document",
        "category": "Mixed Test"
      }
    }
  ]'
```

## Response Format

All bulk operations return a response in this format:
```json
{
  "hasErrors": false,
  "took": 50,
  "successCount": 2,
  "errorCount": 0,
  "items": [
    {
      "operation": "index",
      "id": "generated-id",
      "index": "documents",
      "status": 200,
      "result": "created",
      "error": null,
      "success": true
    }
  ]
}
```

## Prerequisites

1. Java 21
2. Elasticsearch running on localhost:9200
3. Maven

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080
