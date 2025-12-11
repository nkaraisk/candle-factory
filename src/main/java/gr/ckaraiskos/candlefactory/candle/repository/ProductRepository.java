package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByMaterialAndProductCode(Product.materialType material, String productCode);

    List<Product> findAllByDeleted(boolean deleted);

    List<Product> findAllByDeletedAndMaterial(boolean b, Product.materialType materialType);

    List<Product> findAllByDeletedAndProductCode(boolean deleted, String productCode);
}
