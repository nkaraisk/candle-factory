package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import gr.ckaraiskos.candlefactory.candle.service.ProductionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @PostMapping
    public ResponseEntity<Production> create(@RequestBody ProductionDto productionDto) {
        return productionService.create(productionDto);
    }

    @GetMapping
    public ResponseEntity<List<Production>> getAll() {
        return productionService.getAll();
    }

    @DeleteMapping("/{productionId}")
    public ResponseEntity<Void> delete(@PathVariable Long productionId) {
        return productionService.delete(productionId);
    }
}
