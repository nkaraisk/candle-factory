package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, Long> {

    Optional<Production> findByDateOfProductionAndProduct(LocalDate dateOfProduction, Product product);

    List<Production> findAllByProductOrderByDateOfProductionDesc(Product product);

    List<Production> findAllByDateOfProduction(LocalDate dateOfProduction);

    List<Production> findAllByDateOfProductionBetween(LocalDate dateOfProductionAfter, LocalDate dateOfProductionBefore);
}
