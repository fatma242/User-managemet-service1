package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.service.AdminService;

@CrossOrigin("http://localhost:8082")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Endpoint to create companies with auto-generated passwords
    @PostMapping("/createCompanies")
    public String createCompanies(@RequestBody List<String> companyNames) {
        adminService.createCompanies(companyNames);
        return "Companies created successfully";
    }

    // Endpoint to list all customers
    @GetMapping("/listCustomers")
    public List<User> listCustomers() {
        return adminService.listCustomers();
    }

    // Endpoint to list all dish seller companies
    @GetMapping("/listCompanies")
    public List<Company> listCompanies() {
        return adminService.listCompanyRepresentatives();
    }
}