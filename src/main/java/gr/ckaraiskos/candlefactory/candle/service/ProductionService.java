package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.ProductionComponent;
import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private static final Logger log = LoggerFactory.getLogger(ProductionService.class);

    private final ProductionComponent productionComponent;

    public ResponseEntity<Production> create(ProductionDto dto) {
        log.info("Recording production for product {}", dto.getProductId());
        return ResponseEntity.ok(productionComponent.tryAddProduction(dto));
    }

    public ResponseEntity<Production> update(ProductionDto dto) {
        log.info("Updating production {}", dto.getId());
        return ResponseEntity.ok(productionComponent.tryUpdateProduction(dto));
    }

    public ResponseEntity<List<Production>> getAll() {
        return ResponseEntity.ok(productionComponent.tryGetAllProductions());
    }

    public ResponseEntity<List<Production>> getByProduct(Long productId) {
        return ResponseEntity.ok(productionComponent.tryGetProductionByProductId(productId));
    }

    public ResponseEntity<List<Production>> getByDate(LocalDate date) {
        return ResponseEntity.ok(productionComponent.tryGetAllProductionsByDate(date));
    }

    public ResponseEntity<Production> getByDateAndProduct(LocalDate date, Long productId) {
        return ResponseEntity.ok(productionComponent.tryGetProductionByDateAndProductId(date, productId));
    }

    public ResponseEntity<Void> delete(Long productionId) {
        log.info("Deleting production {}", productionId);
        productionComponent.tryRemoveProduction(productionId);
        return ResponseEntity.noContent().build();
    }
}
