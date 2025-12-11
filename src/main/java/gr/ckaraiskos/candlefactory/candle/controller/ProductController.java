package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ProductDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import gr.ckaraiskos.candlefactory.candle.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto productDto) throws EntityAlreadyExistsException {
        log.info("Received adding request for product");

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.newProduct(productDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<Product> editProduct(@Valid @RequestBody ProductDto productDto) throws EntityNotFoundException, EntityAlreadyExistsException {
        log.info("Received edit request for product");

        return ResponseEntity.ok().body(productService.updateProduct(productDto));
    }

    @DeleteMapping("/{productId}/admin/delete")
    public ResponseEntity<Void> deleteHProduct(@PathVariable Long productId) throws EntityNotFoundException, FailedDeletionException, DataIntegrityViolationException {
        log.info("Received HARD delete request for product");

        productService.hardDeleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<Void> deleteSProduct(@PathVariable Long productId) throws EntityNotFoundException, FailedDeletionException {
        log.info("Received Soft delete request for product");

        productService.softDeleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() throws EntityNotFoundException {
        log.info("Received GET all products");

        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @GetMapping("/{productMaterial}/getMaterial")
    public ResponseEntity<List<Product>> getProductByMaterial(@PathVariable Product.materialType productMaterial) throws EntityNotFoundException {
        log.info("Received GET product by material type");

        return ResponseEntity.ok().body(productService.getAllProductsByMaterial(productMaterial));
    }

    @GetMapping("/{productCode}/getCode")
    public ResponseEntity<List<Product>> getProductByCode(@PathVariable String productCode) throws EntityNotFoundException {
        log.info("Received GET product by code");

        return ResponseEntity.ok().body(productService.getAllProductsByCode(productCode));
    }

    @GetMapping("/getSpecific")
    public ResponseEntity<Product> getSpecificProducts(@RequestParam Product.materialType productMaterial, @RequestParam String productCode) throws  EntityNotFoundException {
        log.info("Received GET Specific product");

        return ResponseEntity.ok().body(productService.getSpecificProduct(productMaterial, productCode));
    }
}
