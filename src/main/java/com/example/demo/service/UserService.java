package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private EntityManager em;

    // Register a new user
    public ResponseEntity<?> register(User user) {
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username OR u.email = :email", User.class);
            query.setParameter("username", user.getUsername());
            query.setParameter("email", user.getEmail());

            if (!query.getResultList().isEmpty()) {
                return ResponseEntity.status(409).body("Username or Email already exists.");
            }

            em.persist(user);
            return ResponseEntity.status(201).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during registration: " + e.getMessage());
        }
    }

    // Login a user
    public ResponseEntity<?> login(String username, String password) {
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);

            List<User> users = query.getResultList();

            if (users.isEmpty() || !users.get(0).getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid username or password.");
            }

            return ResponseEntity.ok(users.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during login: " + e.getMessage());
        }
    }

    // Get all users
    public ResponseEntity<?> getAllUsers() {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
            List<User> users = query.getResultList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching users: " + e.getMessage());
        }
    }

    // Find user by ID
    public User findCustomerById(Long id) {
        return em.find(User.class, id);
    }

    public User getSellerById(Long id) {
        return em.find(User.class, id);
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
}
