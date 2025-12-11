package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {


    Optional<Storage> findStorageByProduct(Product product);

    List<Storage> findAllByProduct_Material(Product.materialType productMaterial);

    List<Storage> findAllByProduct_ProductCode(String productProductCode);
}
