package com.klu.Controllers;

import com.klu.Models.Products;
import com.klu.DTO.AddPhotoViewModel;
import com.klu.Interfaces.IProductRepository;
import com.klu.Interfaces.IPhotoServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final IProductRepository productRepository;
    private final IPhotoServices photoService;

    @Autowired
    public ProductsController(IProductRepository productRepository, IPhotoServices photoService) {
        this.productRepository = productRepository;
        this.photoService = photoService;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<Products>> getProducts() {
        return ResponseEntity.ok(productRepository.getAllProducts());
    }

    // GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        Optional<Products> product = productRepository.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Product not found"));
        }
        return ResponseEntity.ok(product);
    }

    // GET related products
    @GetMapping("/related")
    public ResponseEntity<?> getRelatedProducts(
            @RequestParam String category,
            @RequestParam String subCategory,
            @RequestParam int excludeId) {

        List<Products> products = productRepository.findRelatedProducts(category, subCategory, excludeId);
        if (products == null || products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No related products found"));
        }
        return ResponseEntity.ok(products);
    }

    // POST new product
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postProduct(@ModelAttribute AddPhotoViewModel vm) {
        if (vm == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Invalid product data"));
        }

        MultipartFile image = vm.getImage();
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Image is required"));
        }

        String uploadedUrl;
        try {
            Map<?, ?> uploadResult = photoService.uploadImage(image);
            uploadedUrl = uploadResult.get("url").toString();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Image upload failed: " + ex.getMessage()));
        }

        // Map DTO to entity
        Products product = new Products();
        product.setName(vm.getName());
        product.setPrice(vm.getPrice());
        product.setCategory(vm.getCategory());
        product.setSubCategory(vm.getSubCategory());
        product.setImage(uploadedUrl);
        product.setWidth(vm.getWidth());
        product.setHeight(vm.getHeight());
        product.setDescription(vm.getDescription());

        productRepository.addProduct(product);

        return ResponseEntity.created(URI.create("/api/products/" + product.getId()))
                .body(product);
    }

    // PUT update product
    @PutMapping("/{id}")
    public ResponseEntity<?> putProduct(@PathVariable int id, @RequestBody Products product) {
        if (product == null || id != product.getId()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Product ID mismatch"));
        }

        Optional<Products> existing = productRepository.getProductById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Product not found"));
        }

        productRepository.updateProduct(product);
        return ResponseEntity.ok(Collections.singletonMap("message", "Product updated successfully"));
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        Optional<Products> existing = productRepository.getProductById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Product not found"));
        }

        productRepository.deleteProduct(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Product deleted successfully"));
    }
}
