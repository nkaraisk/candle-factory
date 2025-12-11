package gr.ckaraiskos.candlefactory.candle.component;

import gr.ckaraiskos.candlefactory.candle.dto.ProductDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import gr.ckaraiskos.candlefactory.candle.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductComponent {

    private final StorageComponent storageComponent;
    private final ProductRepository productRepository;

    @Transactional
    public Product tryAddProduct(ProductDto newProductDto) throws EntityAlreadyExistsException {
        log.info("Trying to add product.");

        log.info("Checking if product exists.");
        Optional<Product> optionalProduct = productRepository.findByMaterialAndProductCode(newProductDto.getMaterial(), newProductDto.getProductCode());
        if (optionalProduct.isPresent() && !optionalProduct.get().isDeleted()) {
            log.error("Product already exists.");
            throw new EntityAlreadyExistsException("Product already exists.");
        } else if (optionalProduct.isPresent() && optionalProduct.get().isDeleted()) {
            log.error("Product is deleted. So we reinstate");
            Product existing = optionalProduct.get();
            existing.setDeleted(false);

            // ΕΝΗΜΕΡΩΣΗ με τα νέα δεδομένα που έστειλε ο χρήστης
            existing.setPrice(newProductDto.getPrice());
            existing.setByWeight(newProductDto.isByWeight());

            productRepository.save(existing);

            return optionalProduct.get();
        }

        Product newProduct = Product.builder().material(newProductDto.getMaterial())
                .productCode(newProductDto.getProductCode())
                .price(newProductDto.getPrice())
                .byWeight(newProductDto.isByWeight())
                .build();

        log.info("Saving product.");
        productRepository.save(newProduct);
        log.info("Added product Successfully.");

        log.info("Initiating storage for new product.");
        storageComponent.initiateStorage(newProduct);

        return newProduct;
    }

    @Transactional
    public Product tryUpdateProduct(ProductDto updateProductDto) throws EntityNotFoundException, EntityAlreadyExistsException {
        log.info("Trying to update product.");

        log.info("Checking if product exists.");
        Product existingProduct = productRepository.findById(updateProductDto.getProductId())
                .orElseThrow(() -> {
                    log.error("Product with id {} not found.", updateProductDto.getProductId());
                    return new EntityNotFoundException("Product with id:" +updateProductDto.getProductId() + "not found.");
                });

        if (existingProduct.isDeleted()) {
            log.error("Product is deleted.");
            throw new EntityNotFoundException("Product is deleted.");
        }

        boolean isMaterialChanged = existingProduct.getMaterial() != updateProductDto.getMaterial();
        boolean isCodeChanged = !existingProduct.getProductCode().equals(updateProductDto.getProductCode());

        if (isMaterialChanged || isCodeChanged) {
            Optional<Product> duplicateCheck = productRepository.findByMaterialAndProductCode(
                    updateProductDto.getMaterial(),
                    updateProductDto.getProductCode()
            );

            // Αν βρέθηκε προϊόν ΚΑΙ δεν είναι ο εαυτός μας (διαφορετικό ID)
            if (duplicateCheck.isPresent() && !duplicateCheck.get().getId().equals(existingProduct.getId())) {
                throw new EntityAlreadyExistsException("Another product with this Code and Material already exists.");
            }
        }
        log.info("Successfully retrieved product. Updating...");

        existingProduct.setMaterial(updateProductDto.getMaterial());
        existingProduct.setProductCode(updateProductDto.getProductCode());
        existingProduct.setPrice(updateProductDto.getPrice());
        existingProduct.setByWeight(updateProductDto.isByWeight());
        existingProduct.setDeleted(false);
        productRepository.save(existingProduct);

        log.info("Updated product Successfully.");
        return existingProduct;
    }

    @Transactional
    public void tryHardDeleteProduct(Long productId) throws EntityNotFoundException, FailedDeletionException, DataIntegrityViolationException {
        log.info("Trying to HARD delete product.");

        log.info("Checking if product exists.");
        Product deletingProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found.", productId);
                    return new EntityNotFoundException("Product with id:" +productId + "not found.");
                });

        log.info("Deleting product storage");
        storageComponent.deleteByProduct(deletingProduct);

        log.info("Successfully retrieved product. Deleting...");
        productRepository.deleteById(productId);

        log.info("Hard deleted product successfully.");
    }

    @Transactional
    public void trySoftDeleteProduct(Long productId) throws EntityNotFoundException {
        log.info("Trying to SOFT delete product.");

        log.info("Checking if product exists.");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found.", productId);
                    return new EntityNotFoundException("Product with id:" +productId + "not found.");
                });

        // Αντί για delete, το μαρκάρω ως διαγεγραμμένο
        product.setDeleted(true);

        log.info("Successfully SOFT deleted product.");
        productRepository.save(product);
    }

    public List<Product> tryGetAllProducts() throws EntityNotFoundException {
        log.info("Trying to get all products");

        List<Product> products = productRepository.findAllByDeleted(false);
        if (products.isEmpty()) {
            log.error("No products found.");
            throw new EntityNotFoundException("No products found.");
        }

        log.info("Successfully retrieved all products.");
        return products;
    }

    public List<Product> tryGetAllProductsByMaterial(Product.materialType materialType) throws EntityNotFoundException {
        log.info("Trying to get all products by material type");

        List<Product> products = productRepository.findAllByDeletedAndMaterial(false, materialType);
        if (products.isEmpty()) {
            log.error("No products found.");
            throw new EntityNotFoundException("No products found.");
        }

        log.info("Successfully retrieved all products by material type.");
        return products;
    }

    public List<Product> tryGetAllProductsByProductCode(String productCode) throws EntityNotFoundException {
        log.info("Trying to get all products by product code");

        List<Product> products = productRepository.findAllByDeletedAndProductCode(false, productCode);
        if (products.isEmpty()) {
            log.error("No products found.");
            throw new EntityNotFoundException("No products found.");
        }

        log.info("Successfully retrieved all products by product code.");
        return products;
    }

    public Product tryGetSpecificProduct(Product.materialType material, String productCode) throws EntityNotFoundException {
        log.info("Trying to get specific product by material type and product code");

        Optional<Product> product = productRepository.findByMaterialAndProductCode(material, productCode);
        if (product.isPresent() && !product.get().isDeleted()) {
            log.info("Successfully retrieved product by material and product code.");
            return product.get();
        }

        log.error("Product with material: {} and product code: {} not found.",  material, productCode);
        throw new EntityNotFoundException("Product with material: " + material + " and product code: " + productCode + " not found.");
    }

    public Product tryFindProduct(Long productId) throws EntityNotFoundException {
        log.info("Trying to find product with id {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found.", productId);
                    return new EntityNotFoundException("Product with id:" + productId + " not found.");
        });

        return product;
    }
}
