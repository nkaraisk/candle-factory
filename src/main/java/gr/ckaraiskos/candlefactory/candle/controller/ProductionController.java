package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import gr.ckaraiskos.candlefactory.candle.service.ProductionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/production")
public class ProductionController {

    private final ProductionService productionService;

    @Autowired
    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping("/add")
    public ResponseEntity<Production> addProduction(@Valid @RequestBody ProductionDto productionDto) throws EntityAlreadyExistsException {
        log.info("Received adding request for production.");

        return ResponseEntity.ok().body(productionService.newProduction(productionDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<Production> editProduction(@Valid @RequestBody ProductionDto productionDto) throws EntityNotFoundException {
        log.info("Received edit request for production.");

        return ResponseEntity.ok().body(productionService.updateProduction(productionDto));
    }

    @DeleteMapping("/{productionId}/delete")
    public ResponseEntity<Void> deleteProduction(@PathVariable("productionId") Long productionId) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Received delete request for production.");
        productionService.removeProduction(productionId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Production>> getAllProductions() {
        log.info("Received getAll production request.");

        return ResponseEntity.ok().body(productionService.getAll());
    }

    @GetMapping("/{productId}/getProduct")
    public ResponseEntity<List<Production>> getProductionByProduct(@PathVariable("productId") Long productId) throws EntityNotFoundException {
        log.info("Received GET production by product request.");

        return ResponseEntity.ok().body(productionService.getByProductId(productId));
    }

    @GetMapping("/{date}/getDate")
    public ResponseEntity<List<Production>> getProductionByDate(@PathVariable("date") LocalDate date) {
        log.info("Received GET production by date request.");

        return ResponseEntity.ok().body(productionService.getByDate(date));
    }

    @GetMapping("/getSpecific")
    public ResponseEntity<Production> getSpecificProduction(@RequestParam LocalDate date, @RequestParam Long productId) throws EntityNotFoundException {
        log.info("Received GET production by date and product request.");

        return ResponseEntity.ok().body(productionService.getByDateAndProductId(date, productId));
    }

    @GetMapping("/getDateRange")
    public ResponseEntity<List<Production>> getDateRange(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        log.info("Received GET production by date range request.");

        return  ResponseEntity.ok().body(productionService.getByDateRange(fromDate, toDate));
    }

}
