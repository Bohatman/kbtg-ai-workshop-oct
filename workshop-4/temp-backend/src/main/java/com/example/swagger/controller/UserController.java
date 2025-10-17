package com.example.swagger.controller;

import com.example.swagger.dto.ApiResponse;
import com.example.swagger.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing user data")
public class UserController {

    @Operation(
        summary = "Get all users",
        description = "Retrieve a list of all users in the system. This endpoint returns user information including their personal details."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Successful Response",
                    summary = "Example of successful user retrieval",
                    description = "This example shows a successful response when fetching all users",
                    value = """
                        {
                          "status": "success",
                          "message": "Users retrieved successfully",
                          "data": [
                            {
                              "id": 1,
                              "name": "John Doe",
                              "email": "john.doe@example.com",
                              "age": 30,
                              "phoneNumber": "+1-555-123-4567"
                            },
                            {
                              "id": 2,
                              "name": "Jane Smith",
                              "email": "jane.smith@example.com",
                              "age": 28,
                              "phoneNumber": "+1-555-987-6543"
                            }
                          ]
                        }
                        """
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = Arrays.asList(
            new User(1L, "John Doe", "john.doe@example.com", 30, "+1-555-123-4567"),
            new User(2L, "Jane Smith", "jane.smith@example.com", 28, "+1-555-987-6543")
        );
        
        return ResponseEntity.ok(
            ApiResponse.success("Users retrieved successfully", users)
        );
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve a specific user by their unique identifier"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Found",
                    summary = "Example of successful user retrieval by ID",
                    value = """
                        {
                          "status": "success",
                          "message": "User found",
                          "data": {
                            "id": 1,
                            "name": "John Doe",
                            "email": "john.doe@example.com",
                            "age": 30,
                            "phoneNumber": "+1-555-123-4567"
                          }
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Not Found",
                    summary = "Example when user is not found",
                    value = """
                        {
                          "status": "error",
                          "message": "User not found with ID: 999",
                          "data": null
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(
        @Parameter(
            description = "User's unique identifier",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ) {
        if (id == 1L) {
            User user = new User(1L, "John Doe", "john.doe@example.com", 30, "+1-555-123-4567");
            return ResponseEntity.ok(ApiResponse.success("User found", user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Create a new user",
        description = "Create a new user in the system with the provided information"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Created",
                    summary = "Example of successful user creation",
                    value = """
                        {
                          "status": "success",
                          "message": "User created successfully",
                          "data": {
                            "id": 3,
                            "name": "Alice Johnson",
                            "email": "alice.johnson@example.com",
                            "age": 25,
                            "phoneNumber": "+1-555-456-7890"
                          }
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation Error",
                    summary = "Example of validation error",
                    value = """
                        {
                          "status": "error",
                          "message": "Validation failed: Email is required",
                          "data": null
                        }
                        """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User information to create",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = {
                    @ExampleObject(
                        name = "Complete User",
                        summary = "Example with all user fields",
                        description = "This example shows how to create a user with all available fields",
                        value = """
                            {
                              "id": 3,
                              "name": "Alice Johnson",
                              "email": "alice.johnson@example.com",
                              "age": 25,
                              "phoneNumber": "+1-555-456-7890"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Minimal User",
                        summary = "Example with required fields only",
                        description = "This example shows the minimum required fields to create a user",
                        value = """
                            {
                              "id": 4,
                              "name": "Bob Wilson",
                              "email": "bob.wilson@example.com"
                            }
                            """
                    )
                }
            )
        )
        @Valid @RequestBody User user
    ) {
        // Simulate user creation
        return ResponseEntity.ok(
            ApiResponse.success("User created successfully", user)
        );
    }

    @Operation(
        summary = "Update an existing user",
        description = "Update user information for an existing user"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Updated",
                    summary = "Example of successful user update",
                    value = """
                        {
                          "status": "success",
                          "message": "User updated successfully",
                          "data": {
                            "id": 1,
                            "name": "John Updated Doe",
                            "email": "john.updated@example.com",
                            "age": 31,
                            "phoneNumber": "+1-555-123-4567"
                          }
                        }
                        """
                )
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
        @Parameter(
            description = "ID of the user to update",
            example = "1"
        )
        @PathVariable Long id,
        
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated user information",
            content = @Content(
                examples = @ExampleObject(
                    name = "Update Example",
                    summary = "Example of user update data",
                    value = """
                        {
                          "id": 1,
                          "name": "John Updated Doe",
                          "email": "john.updated@example.com",
                          "age": 31,
                          "phoneNumber": "+1-555-123-4567"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody User user
    ) {
        user.setId(id);
        return ResponseEntity.ok(
            ApiResponse.success("User updated successfully", user)
        );
    }

    @Operation(
        summary = "Delete a user",
        description = "Delete a user from the system by their ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User deleted successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Deleted",
                    summary = "Example of successful user deletion",
                    value = """
                        {
                          "status": "success",
                          "message": "User deleted successfully",
                          "data": null
                        }
                        """
                )
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
        @Parameter(
            description = "ID of the user to delete",
            example = "1"
        )
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            ApiResponse.success("User deleted successfully", null)
        );
    }
}