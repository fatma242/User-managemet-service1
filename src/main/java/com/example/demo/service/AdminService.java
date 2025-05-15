package com.example.demo.service;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.model.Company;
import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class AdminService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;

    @Transactional
    public ResponseEntity<String> createCompanies(List<String> companyNames) {
        for (String name : companyNames) {
            String cleanName = name.trim();
            entityManager.flush(); // ensure DB sync
            Company existing = findCompanyByName(cleanName);
            if (existing != null) {
                return new ResponseEntity<>("Company with name " + cleanName + " already exists", HttpStatus.CONFLICT);
            }

            Company newCompany = new Company();
            newCompany.setName(cleanName);

            // Manually generate the password here instead of relying on @PrePersist
            String generatedPassword = String.valueOf(100000 + new Random().nextInt(900000));
            newCompany.setPassword(generatedPassword);

            // Now the password is definitely not null
            User user = new User();
            user.setUsername(cleanName);
            user.setPassword(generatedPassword);
            user.setRole("SELLER");
            userService.register(user);

            entityManager.persist(newCompany);
        }

        return new ResponseEntity<>("Companies created successfully", HttpStatus.CREATED);
    }

    private Company findCompanyByName(String name) {
        TypedQuery<Company> query = entityManager.createQuery(
                "SELECT c FROM Company c WHERE c.name = :name", Company.class);
        query.setParameter("name", name);

        List<Company> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * List all customers (users with role CUSTOMER)
     */
    public List<User> listCustomers() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.role = 'CUSTOMER'", User.class);
        return query.getResultList();
    }

    /**
     * List all company representatives (i.e., dish seller companies)
     */
    public List<Company> listCompanyRepresentatives() {
        TypedQuery<Company> query = entityManager.createQuery(
                "SELECT c FROM Company c", Company.class);
        return query.getResultList();
    }
}