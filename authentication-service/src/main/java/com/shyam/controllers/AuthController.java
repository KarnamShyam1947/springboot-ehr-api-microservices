package com.shyam.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @GetMapping
    public ResponseEntity<Map<String, String>> check() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of("hello", "world")
                );
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of("hello", "world")
                );
    }
   
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of("hello", "world")
                );
    }

}
