package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.SaleDto;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.StorageViolationException;
import gr.ckaraiskos.candlefactory.candle.service.SaleService;
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
@RequestMapping("/sale")
public class SaleController {

    private SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/add")
    ResponseEntity<Sale> addSale(@Valid @RequestBody SaleDto saleDto) throws EntityAlreadyExistsException, EntityNotFoundException, StorageViolationException {
        log.info("Received adding request for sale.");

        return ResponseEntity.ok().body(saleService.newSale(saleDto));
    }

    @PutMapping("/edit")
    ResponseEntity<Sale> editSale(@Valid @RequestBody SaleDto saleDto) throws EntityNotFoundException, StorageViolationException {
        log.info("Received edit request for sale.");

        return ResponseEntity.ok().body(saleService.updateSale(saleDto));
    }

    @DeleteMapping("/{saleId}/delete")
    ResponseEntity<Void> deleteSale(@PathVariable("saleId") Long saleId) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Received delete request for sale.");

        saleService.deleteSaleById(saleId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    ResponseEntity<List<Sale>> getAllSales() {
        log.info("Received getAll sales request.");

        return ResponseEntity.ok().body(saleService.getAll());
    }

    @GetMapping("{customerId}/getCustomer")
    ResponseEntity<List<Sale>> getSalesByCustomer(@PathVariable("customerId") Long customerId) {
        log.info("Received getSalesByCustomer request.");

        return ResponseEntity.ok().body(saleService.getAllByCustomerId(customerId));
    }

    @GetMapping("/{productId}/getProduct")
    ResponseEntity<List<Sale>> getSalesByProduct(@PathVariable("productId") Long productId) {
        log.info("Received getSalesByProduct request.");

        return ResponseEntity.ok().body(saleService.getAllByProductId(productId));
    }

    @GetMapping("/{date}/getDate")
    ResponseEntity<List<Sale>> getSalesByDate(@PathVariable("date") LocalDate date) {
        log.info("Received getSalesByDate request.");

        return ResponseEntity.ok().body(saleService.getAllByDate(date));
    }

    @GetMapping("/getCustomerProduct")
    ResponseEntity<List<Sale>> getSalesByCustomerProduct(@RequestParam Long customerId, @RequestParam Long productId) {
        log.info("Received getSalesByCustomerProduct request.");

        return ResponseEntity.ok().body(saleService.getAllByCustomerIdAndProductId(customerId, productId));
    }

    @GetMapping("/getCustomerDate")
    ResponseEntity<List<Sale>> getSalesByCustomerDate(@RequestParam Long customerId, @RequestParam LocalDate date) {
        log.info("Received getSalesByCustomerDate request.");

        return ResponseEntity.ok().body(saleService.getAllByCustomerIdAndDate(customerId, date));
    }

    @GetMapping("/getProductDate")
    ResponseEntity<List<Sale>> getSalesByProductDate(@RequestParam Long productId, @RequestParam LocalDate date) {
        log.info("Received getSalesByProductDate request.");

        return ResponseEntity.ok().body(saleService.getAllByProductIdAndDate(productId, date));
    }

    @GetMapping("/getByAll")
    ResponseEntity<List<Sale>> getSalesByAll(@RequestParam Long customerId, @RequestParam Long productId, @RequestParam LocalDate date) {
        log.info("Received getSalesByAll request.");

        return ResponseEntity.ok().body(saleService.getByAll(customerId, productId, date));
    }
}
