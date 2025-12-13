package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.ProductComponent;
import gr.ckaraiskos.candlefactory.candle.dto.ProductDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductComponent productComponent;

    public ResponseEntity<Product> createProduct(ProductDto dto) {
        log.info("Registering product {}", dto.getProductCode());
        return ResponseEntity.ok(productComponent.tryAddProduct(dto));
    }

    public ResponseEntity<List<Product>> getAll() {
        log.info("Getting all products");
        return ResponseEntity.ok(productComponent.tryGetAllProducts());
    }

    public ResponseEntity<Product> update(Long productId, ProductDto dto) {
        log.info("Updating product {}", productId);
        dto.setProductId(productId);
        return ResponseEntity.ok(productComponent.tryUpdateProduct(dto));
    }

    public ResponseEntity<Void> delete(Long productId) {
        log.info("Deleting product {}", productId);
        productComponent.trySoftDeleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
