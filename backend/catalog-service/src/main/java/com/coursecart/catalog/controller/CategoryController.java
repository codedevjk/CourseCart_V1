package com.coursecart.catalog.controller;

import com.coursecart.catalog.dto.CategoryDTO;
import com.coursecart.catalog.dto.CategoryRequest;
import com.coursecart.catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/categories")
public class CategoryController {

    private final CatalogService catalogService;

    public CategoryController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delegate fetching all categories to CatalogService (US 04).");
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delegate creating category to CatalogService (US 04).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delegate updating category to CatalogService (US 04).");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delegate deleting category to CatalogService (US 04).");
    }
}
