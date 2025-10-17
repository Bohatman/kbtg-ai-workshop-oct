# Swagger Example API - Python FastAPI Version

This is a Python FastAPI version of the Java Spring Boot application, demonstrating comprehensive API documentation with Swagger/OpenAPI, SQLite database integration, and CRUD operations for user management.

## Features

- FastAPI with Python 3.8+
- SQLite database with SQLAlchemy ORM
- Comprehensive API documentation with Swagger UI
- CRUD operations for User management
- Points management system
- Membership levels (BRONZE, SILVER, GOLD, PLATINUM)
- Email validation and uniqueness constraints
- Automatic timestamps (created_at, updated_at)

## Project Structure

```
python-temp/
├── main.py                 # FastAPI application entry point
├── requirements.txt        # Python dependencies
├── database.py            # SQLite database configuration
├── models.py              # SQLAlchemy models
├── schemas.py             # Pydantic schemas for validation
└── routers/
    ├── hello.py           # Hello world endpoints
    └── users.py           # User CRUD endpoints
```

## Installation

1. Create virtual environment:
   ```bash
   cd /Users/puttipong.s/demo/kbtg-ai-workshop-oct/workshop-4/python-temp
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Run the application:
   ```bash
   python main.py
   ```

   Or with uvicorn directly:
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 3000 --reload
   ```

## API Endpoints

### Hello World Endpoints
- `GET /hello/` - Hello world message
- `GET /hello/health` - Application health check

### User Management Endpoints
- `GET /users/` - Get all users
- `GET /users/{user_id}` - Get user by ID
- `POST /users/` - Create new user
- `PUT /users/{user_id}` - Update existing user
- `DELETE /users/{user_id}` - Delete user

### Points Management Endpoints
- `POST /users/{user_id}/points/add` - Add points to user
- `POST /users/{user_id}/points/deduct` - Deduct points from user

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:3000/docs
- **ReDoc**: http://localhost:3000/redoc
- **OpenAPI JSON**: http://localhost:3000/openapi.json

## User Model

```python
{
  "id": 1,
  "first_name": "สมชาย",
  "last_name": "ใจดี", 
  "phone": "081-234-5678",
  "email": "somchai@example.com",
  "member_since": "2024-01-15T10:30:00",
  "membership_level": "GOLD",
  "points": 1500,
  "created_at": "2024-01-15T10:30:00",
  "updated_at": "2024-01-15T10:30:00"
}
```

## Example Usage

### Create User
```bash
curl -X POST "http://localhost:3000/users/" \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "สมชาย",
    "last_name": "ใจดี",
    "phone": "081-234-5678", 
    "email": "somchai@example.com",
    "membership_level": "SILVER",
    "points": 100
  }'
```

### Get All Users
```bash
curl -X GET "http://localhost:3000/users/"
```

### Add Points
```bash
curl -X POST "http://localhost:3000/users/1/points/add" \
  -H "Content-Type: application/json" \
  -d '{"points": 100}'
```

## Database

The application uses SQLite database (`users.db`) which will be created automatically when you first run the application. The database schema includes:

- **users table** with all user information
- Automatic timestamps for record tracking
- Email uniqueness constraint
- Membership level enumeration

## Key Features Comparison with Java Version

| Feature | Java Spring Boot | Python FastAPI |
|---------|------------------|----------------|
| Web Framework | Spring Boot | FastAPI |
| Database ORM | JPA/Hibernate | SQLAlchemy |
| Validation | Jakarta Validation | Pydantic |
| API Documentation | SpringDoc OpenAPI | FastAPI built-in |
| Dependency Injection | Spring DI | FastAPI Depends |
| Database | SQLite | SQLite |
| Port | 3000 | 3000 |

Both versions provide identical functionality and API compatibility.