package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import gr.ckaraiskos.candlefactory.candle.service.ProductionService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/add")
    public ResponseEntity<Production> legacyCreate(@RequestBody ProductionDto productionDto) {
        return productionService.create(productionDto);
    }

    @PutMapping("/edit")
    public ResponseEntity<Production> legacyUpdate(@RequestBody ProductionDto productionDto) {
        return productionService.update(productionDto);
    }

    @GetMapping
    public ResponseEntity<List<Production>> getAll() {
        return productionService.getAll();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Production>> legacyGetAll() {
        return productionService.getAll();
    }

    @GetMapping("/{productId}/getProduct")
    public ResponseEntity<List<Production>> getByProduct(@PathVariable Long productId) {
        return productionService.getByProduct(productId);
    }

    @GetMapping("/{date}/getDate")
    public ResponseEntity<List<Production>> getByDate(@PathVariable LocalDate date) {
        return productionService.getByDate(date);
    }

    @GetMapping("/getSpecific")
    public ResponseEntity<Production> getSpecificProduction(@RequestParam LocalDate date, @RequestParam Long productId) {
        return productionService.getByDateAndProduct(date, productId);
    }

    @DeleteMapping("/{productionId}")
    public ResponseEntity<Void> delete(@PathVariable Long productionId) {
        return productionService.delete(productionId);
    }

    @DeleteMapping("/{productionId}/delete")
    public ResponseEntity<Void> legacyDelete(@PathVariable Long productionId) {
        return productionService.delete(productionId);
    }
}
