# Swagger Example Description Demo

This project demonstrates how to use Swagger annotations to set examples and descriptions in a Spring Boot REST API, following the Baeldung tutorial on Swagger Set Example Description.

## Features

- Spring Boot 3.x with Java 17
- SpringDoc OpenAPI 3 (Swagger)
- Comprehensive Swagger annotations with examples and descriptions
- CRUD operations for User management
- Multiple example objects for different scenarios
- Detailed API documentation

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/swagger/
│   │       ├── SwaggerExampleApplication.java  # Main application class
│   │       ├── config/
│   │       │   └── OpenApiConfig.java          # OpenAPI configuration
│   │       ├── controller/
│   │       │   └── UserController.java         # REST controller with Swagger annotations
│   │       ├── dto/
│   │       │   └── ApiResponse.java            # Response wrapper DTO
│   │       └── model/
│   │           └── User.java                   # User entity with schema annotations
│   └── resources/
│       └── application.properties              # Application configuration
```

## Key Swagger Annotations Used

### 1. Schema Annotations (@Schema)
- `@Schema(description = "...")` - Adds descriptions to model fields
- `@Schema(example = "...")` - Sets example values for fields
- `@Schema(required = true)` - Marks fields as required

### 2. Operation Annotations (@Operation)
- `@Operation(summary = "...")` - Short description of the endpoint
- `@Operation(description = "...")` - Detailed description of the endpoint

### 3. Response Annotations (@ApiResponse)
- `@ApiResponse(responseCode = "200", description = "...")` - Describes response codes
- `@Content(examples = @ExampleObject(...))` - Provides response examples

### 4. Parameter Annotations (@Parameter)
- `@Parameter(description = "...", example = "...")` - Describes path/query parameters

### 5. RequestBody Annotations (@RequestBody)
- Multiple examples with `@ExampleObject` arrays
- Different scenarios (complete vs minimal data)

## Running the Application

1. Build the project:
   ```bash
   mvn clean compile
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. Access the application:
   ```
   http://localhost:3000
   ```

4. Access Swagger UI:
   ```
   http://localhost:3000/swagger-ui.html
   ```

5. Access OpenAPI JSON:
   ```
   http://localhost:3000/api-docs
   ```

## API Endpoints

### Default Endpoints
- `GET /` - Hello World message

### Create User with Complete Data
```json
{
  "id": 3,
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "age": 25,
  "phoneNumber": "+1-555-456-7890"
}
```

### Create User with Minimal Data
```json
{
  "id": 4,
  "name": "Bob Wilson",
  "email": "bob.wilson@example.com"
}
```

## Key Learning Points

1. **@Schema annotation** - Used for model documentation and examples
2. **@ExampleObject** - Provides multiple examples with descriptions
3. **@Operation** - Documents endpoint purpose and behavior
4. **@ApiResponse** - Documents different response scenarios
5. **@Parameter** - Documents request parameters with examples

This implementation follows best practices for API documentation and provides comprehensive examples for different use cases.