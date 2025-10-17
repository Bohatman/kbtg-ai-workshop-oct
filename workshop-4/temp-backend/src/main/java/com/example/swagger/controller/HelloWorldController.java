package com.example.swagger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "Hello World", description = "Simple greeting endpoints")
public class HelloWorldController {

    @Operation(
        summary = "Hello World endpoint",
        description = "Returns a simple hello world message"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject(
                    name = "Hello World Response",
                    summary = "Simple greeting message",
                    value = "Hello World!"
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }

    @Operation(
        summary = "Health check endpoint",
        description = "Returns application status"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Application is running",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Health Status",
                    summary = "Application health status",
                    value = """
                        {
                          "status": "UP",
                          "message": "Application is running successfully",
                          "port": 3000
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String message = "Application is running successfully";
            public final int port = 3000;
        });
    }
}