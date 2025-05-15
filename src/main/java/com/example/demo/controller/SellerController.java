package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Dish;
import com.example.demo.service.SellerService;

@CrossOrigin("http://localhost:8082")
@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @GetMapping("/dishes/{sellerId}")
    public ResponseEntity<?> getDishesBySellerId(@PathVariable String sellerId) {
        try {
            // Convert sellerId to Long
            Long id = Long.parseLong(sellerId);
            List<Dish> dishes = sellerService.getDishes(id);
            return ResponseEntity.ok(dishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch dishes: " + e.getMessage());
        }
    }
}