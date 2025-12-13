package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

    Optional<Production> findByDateOfProductionAndProduct(LocalDate dateOfProduction, Product product);

    List<Production> findAllByProductOrderByDateOfProductionDesc(Product product);

    List<Production> findAllByDateOfProduction(LocalDate date);

    List<Production> findAllByDateOfProductionBetween(LocalDate from, LocalDate to);
}
