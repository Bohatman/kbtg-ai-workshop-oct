package com.example.swagger.controller;

import com.example.swagger.dto.ApiResponse;
import com.example.swagger.model.User;
import com.example.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing user data with SQLite database")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Get all users",
        description = "Retrieve a list of all users from the SQLite database"
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
                    value = """
                        {
                          "status": "success",
                          "message": "Users retrieved successfully",
                          "data": [
                            {
                              "id": 1,
                              "firstName": "สมชาย",
                              "lastName": "ใจดี",
                              "phone": "081-234-5678",
                              "email": "somchai@example.com",
                              "memberSince": "2024-01-15T10:30:00",
                              "membershipLevel": "GOLD",
                              "points": 1500,
                              "createdAt": "2024-01-15T10:30:00",
                              "updatedAt": "2024-01-15T10:30:00"
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
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully", users)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve a specific user by their unique identifier from SQLite database"
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
                            "firstName": "สมชาย",
                            "lastName": "ใจดี",
                            "phone": "081-234-5678",
                            "email": "somchai@example.com",
                            "memberSince": "2024-01-15T10:30:00",
                            "membershipLevel": "GOLD",
                            "points": 1500,
                            "createdAt": "2024-01-15T10:30:00",
                            "updatedAt": "2024-01-15T10:30:00"
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
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("User found", user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to retrieve user: " + e.getMessage()));
        }
    }

    @Operation(
        summary = "Create a new user",
        description = "Create a new user in the SQLite database"
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
                            "id": 1,
                            "firstName": "สมชาย",
                            "lastName": "ใจดี",
                            "phone": "081-234-5678",
                            "email": "somchai@example.com",
                            "memberSince": "2024-01-15T10:30:00",
                            "membershipLevel": "BRONZE",
                            "points": 0,
                            "createdAt": "2024-01-15T10:30:00",
                            "updatedAt": "2024-01-15T10:30:00"
                          }
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data or email already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation Error",
                    summary = "Example of validation error",
                    value = """
                        {
                          "status": "error",
                          "message": "Email already exists: somchai@example.com",
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
                        value = """
                            {
                              "firstName": "สมชาย",
                              "lastName": "ใจดี",
                              "phone": "081-234-5678",
                              "email": "somchai@example.com",
                              "membershipLevel": "SILVER",
                              "points": 100
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Minimal User",
                        summary = "Example with required fields only",
                        value = """
                            {
                              "firstName": "สมศรี",
                              "lastName": "สุขใจ",
                              "email": "somsri@example.com"
                            }
                            """
                    )
                }
            )
        )
        @Valid @RequestBody User user
    ) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create user: " + e.getMessage()));
        }
    }

    @Operation(
        summary = "Update an existing user",
        description = "Update user information for an existing user in SQLite database"
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
                            "firstName": "สมชาย",
                            "lastName": "ใจดี (แก้ไข)",
                            "phone": "081-234-5678",
                            "email": "somchai.updated@example.com",
                            "memberSince": "2024-01-15T10:30:00",
                            "membershipLevel": "GOLD",
                            "points": 2000,
                            "createdAt": "2024-01-15T10:30:00",
                            "updatedAt": "2024-01-15T15:45:30"
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
                    summary = "Example when user to update is not found",
                    value = """
                        {
                          "status": "error",
                          "message": "User not found with id: 999",
                          "data": null
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
                          "firstName": "สมชาย",
                          "lastName": "ใจดี (แก้ไข)",
                          "phone": "081-234-5678",
                          "email": "somchai.updated@example.com",
                          "membershipLevel": "GOLD",
                          "points": 2000
                        }
                        """
                )
            )
        )
        @Valid @RequestBody User userDetails
    ) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", updatedUser)
            );
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update user: " + e.getMessage()));
        }
    }

    @Operation(
        summary = "Delete a user",
        description = "Delete a user from the SQLite database by their ID"
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
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Not Found",
                    summary = "Example when user to delete is not found",
                    value = """
                        {
                          "status": "error",
                          "message": "User not found with id: 999",
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
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to delete user: " + e.getMessage()));
        }
    }

    // Additional endpoints for points management
    @Operation(
        summary = "Add points to user",
        description = "Add points to a user's account"
    )
    @PostMapping("/{id}/points/add")
    public ResponseEntity<ApiResponse<User>> addPoints(
        @Parameter(description = "User ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "Points to add", example = "100")
        @RequestParam Integer points
    ) {
        try {
            User updatedUser = userService.addPoints(id, points);
            return ResponseEntity.ok(
                ApiResponse.success("Points added successfully", updatedUser)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
        summary = "Deduct points from user",
        description = "Deduct points from a user's account"
    )
    @PostMapping("/{id}/points/deduct")
    public ResponseEntity<ApiResponse<User>> deductPoints(
        @Parameter(description = "User ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "Points to deduct", example = "50")
        @RequestParam Integer points
    ) {
        try {
            User updatedUser = userService.deductPoints(id, points);
            return ResponseEntity.ok(
                ApiResponse.success("Points deducted successfully", updatedUser)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}