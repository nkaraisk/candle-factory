package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.SaleComponent;
import gr.ckaraiskos.candlefactory.candle.dto.SaleDto;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleService {

    private static final Logger log = LoggerFactory.getLogger(SaleService.class);

    private final SaleComponent saleComponent;

    public ResponseEntity<Sale> create(SaleDto dto) {
        log.info("Recording sale for customer {} and product {}", dto.getCustomerId(), dto.getProductId());
        return ResponseEntity.ok(saleComponent.tryAddSale(dto));
    }

    public ResponseEntity<List<Sale>> getAll() {
        return ResponseEntity.ok(saleComponent.tryGetAllSales());
    }

    public ResponseEntity<Void> delete(Long saleId) {
        log.info("Deleting sale {}", saleId);
        saleComponent.tryDeleteSale(saleId);
        return ResponseEntity.noContent().build();
    }
}
