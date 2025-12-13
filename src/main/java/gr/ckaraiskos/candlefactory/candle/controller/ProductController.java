package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ProductDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return productService.getAll();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        return productService.update(productId, productDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        return productService.delete(productId);
    }
}
