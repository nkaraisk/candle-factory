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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Product> legacyCreate(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> legacyGetAll() {
        return productService.getAll();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return productService.getAll();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> legacyGetAllV2() {
        return productService.getAll();
    }

    @GetMapping("/material")
    public ResponseEntity<List<Product>> getByMaterial(@RequestParam Product.materialType material) {
        return productService.getByMaterial(material);
    }

    @GetMapping("/{productMaterial}/getMaterial")
    public ResponseEntity<List<Product>> getByMaterialPath(@PathVariable Product.materialType productMaterial) {
        return productService.getByMaterial(productMaterial);
    }

    @GetMapping("/code")
    public ResponseEntity<List<Product>> getByCode(@RequestParam String productCode) {
        return productService.getByCode(productCode);
    }

    @GetMapping("/{productCode}/getCode")
    public ResponseEntity<List<Product>> getByCodePath(@PathVariable String productCode) {
        return productService.getByCode(productCode);
    }

    @GetMapping("/lookup")
    public ResponseEntity<Product> getByMaterialAndCode(@RequestParam Product.materialType material,
                                                        @RequestParam String productCode) {
        return productService.getByMaterialAndCode(material, productCode);
    }

    @GetMapping("/getSpecific")
    public ResponseEntity<Product> getSpecificProducts(@RequestParam Product.materialType productMaterial,
                                                       @RequestParam String productCode) {
        return productService.getByMaterialAndCode(productMaterial, productCode);
    }

    @PostMapping("/edit")
    public ResponseEntity<Product> legacyUpdate(@RequestBody ProductDto productDto) {
        return productService.update(productDto.getProductId(), productDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        return productService.update(productId, productDto);
    }

    @DeleteMapping("/{productId}/admin/delete")
    public ResponseEntity<Void> hardDelete(@PathVariable Long productId) {
        return productService.hardDelete(productId);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<Void> legacySoftDelete(@PathVariable Long productId) {
        return productService.delete(productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        return productService.delete(productId);
    }
}
