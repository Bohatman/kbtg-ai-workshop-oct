package com.example.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Schema(description = "User details for registration")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Unique identifier for the user",
        example = "123",
        required = true
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "User's first name",
        example = "สมชาย",
        required = true
    )
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @Schema(
        description = "User's last name",
        example = "ใจดี",
        required = true
    )
    @NotBlank
    private String lastName;

    @Schema(
        description = "User's phone number",
        example = "081-234-5678"
    )
    private String phone;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "User's email address (unique)",
        example = "somchai@example.com",
        required = true
    )
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @Schema(
        description = "Date when user became a member",
        example = "2024-01-15T10:30:00"
    )
    private LocalDateTime memberSince;

    @Column(nullable = false)
    @Schema(
        description = "User's membership level",
        example = "GOLD",
        allowableValues = {"BRONZE", "SILVER", "GOLD", "PLATINUM"}
    )
    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;

    @Column(nullable = false)
    @Schema(
        description = "Current points balance",
        example = "1500",
        minimum = "0"
    )
    @Min(0)
    private Integer points;

    @Column(nullable = false, updatable = false)
    @Schema(
        description = "Record creation timestamp",
        example = "2024-01-15T10:30:00"
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Schema(
        description = "Record last update timestamp",
        example = "2024-01-15T15:45:30"
    )
    private LocalDateTime updatedAt;

    // Enum for membership levels
    public enum MembershipLevel {
        BRONZE, SILVER, GOLD, PLATINUM
    }

    // Constructors
    public User() {}

    public User(String firstName, String lastName, String phone, String email, 
                MembershipLevel membershipLevel, Integer points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.memberSince = LocalDateTime.now();
        this.membershipLevel = membershipLevel != null ? membershipLevel : MembershipLevel.BRONZE;
        this.points = points != null ? points : 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.memberSince == null) {
            this.memberSince = now;
        }
        if (this.membershipLevel == null) {
            this.membershipLevel = MembershipLevel.BRONZE;
        }
        if (this.points == null) {
            this.points = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDateTime memberSince) {
        this.memberSince = memberSince;
    }

    public MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Helper method to add points
    public void addPoints(Integer pointsToAdd) {
        if (pointsToAdd != null && pointsToAdd > 0) {
            this.points = (this.points != null ? this.points : 0) + pointsToAdd;
        }
    }

    // Helper method to deduct points
    public boolean deductPoints(Integer pointsToDeduct) {
        if (pointsToDeduct != null && pointsToDeduct > 0 && 
            this.points != null && this.points >= pointsToDeduct) {
            this.points -= pointsToDeduct;
            return true;
        }
        return false;
    }
}