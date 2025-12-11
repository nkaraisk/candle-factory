package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.ProductComponent;
import gr.ckaraiskos.candlefactory.candle.dto.ProductDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductComponent productComponent;

    public Product newProduct(ProductDto newProductDto) throws EntityAlreadyExistsException {
        log.info("Start adding procedure");

        return productComponent.tryAddProduct(newProductDto);
    }

    public Product updateProduct(ProductDto updateProductDto) throws EntityNotFoundException,  EntityAlreadyExistsException {
        log.info("Start updating procedure");

        return productComponent.tryUpdateProduct(updateProductDto);
    }

    public void hardDeleteProduct(Long productId) throws EntityNotFoundException, FailedDeletionException, DataIntegrityViolationException {
        log.info("Start HARD deleting procedure");

        productComponent.tryHardDeleteProduct(productId);
    }

    public void softDeleteProduct(Long productId) throws EntityNotFoundException, FailedDeletionException {
        log.info("Start SOFT deleting procedure");

        productComponent.trySoftDeleteProduct(productId);
    }

    public List<Product> getAllProducts() throws EntityNotFoundException {
        log.info("Start getting allProducts procedure");

        return productComponent.tryGetAllProducts();
    }

    public List<Product> getAllProductsByMaterial(Product.materialType productMaterial) throws EntityNotFoundException {
        log.info("Start getting allProductsByMaterial procedure");

        return productComponent.tryGetAllProductsByMaterial(productMaterial);
    }

    public List<Product> getAllProductsByCode(String productCode) throws EntityNotFoundException {
        log.info("Start getting allProductsByCode procedure");

        return productComponent.tryGetAllProductsByProductCode(productCode);
    }

    public Product getSpecificProduct(Product.materialType material, String productCode) throws EntityNotFoundException {
        log.info("Start getting specificProduct procedure");

        return productComponent.tryGetSpecificProduct(material, productCode);
    }
}
