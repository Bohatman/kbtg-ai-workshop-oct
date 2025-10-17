package com.example.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "User details for registration")
public class User {

    @Schema(
        description = "Unique identifier for the user",
        example = "123",
        required = true
    )
    @NotNull
    private Long id;

    @Schema(
        description = "User's full name",
        example = "John Doe",
        required = true
    )
    @NotBlank
    private String name;

    @Schema(
        description = "User's email address",
        example = "john.doe@example.com",
        required = true
    )
    @Email
    @NotBlank
    private String email;

    @Schema(
        description = "User's age",
        example = "30",
        minimum = "18",
        maximum = "100"
    )
    private Integer age;

    @Schema(
        description = "User's phone number",
        example = "+1-555-123-4567"
    )
    private String phoneNumber;

    // Constructors
    public User() {}

    public User(Long id, String name, String email, Integer age, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}