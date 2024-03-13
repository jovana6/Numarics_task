package com.assignment.demoproject.productservice;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RegisterClientHeaders(AuthorizationFactory.class)
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Products retrieved",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get a products based on id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Product retrieved",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a product")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Product created",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Update product")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Product updated",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @Secured("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product updated = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a product")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Product deleted",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @Secured("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search the products")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Product found",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category) {
        return productService.searchProducts(name, description, category);
    }
}
