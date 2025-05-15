package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@CrossOrigin("http://localhost:8082")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    // Get all users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/api/users/{id}", params = "role=customer")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        User userDto = userService.findCustomerById(id);
        return userDto != null ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/api/users/{id}", params = "role=seller")
    public ResponseEntity<User> getSellerById(@PathVariable Long id) {
        User user = userService.getSellerById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

}
