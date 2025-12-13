package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByMaterialAndProductCode(Product.materialType material, String productCode);

    List<Product> findAllByDeleted(boolean deleted);

    List<Product> findAllByDeletedAndMaterial(boolean deleted, Product.materialType material);

    List<Product> findAllByDeletedAndProductCode(boolean deleted, String productCode);
}
