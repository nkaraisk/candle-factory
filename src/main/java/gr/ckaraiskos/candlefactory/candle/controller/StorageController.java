package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.StorageDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.service.StorageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/add")
    public ResponseEntity<Storage> addStorage(@Valid @RequestBody StorageDto storageDto) throws EntityAlreadyExistsException, EntityNotFoundException {
        log.info("Received adding request for storage.");

        return ResponseEntity.ok().body(storageService.newStorage(storageDto));
    }

    @PutMapping("/{storageId}/edit")
    public ResponseEntity<Storage> editStorage(@PathVariable("storageId") Long storageId, @RequestParam double quantity) throws EntityNotFoundException {
        log.info("Received edit request for storage.");

        return ResponseEntity.ok().body(storageService.updateStorage(storageId, quantity));
    }

    @DeleteMapping("{storageId}/delete")
    public ResponseEntity<Void> deleteStorage(@PathVariable("storageId") Long storageId) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Received delete request for storage.");

        storageService.deleteStorage(storageId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Storage>> getAllStorage() {
        log.info("Received getAll request for storage.");

        return ResponseEntity.ok().body(storageService.getAll());
    }

    @GetMapping("{material}/getMaterial")
    public ResponseEntity<List<Storage>> getStorageByMaterial(@PathVariable("material") Product.materialType material) {
        log.info("Received GET by material request for storage.");

        return  ResponseEntity.ok().body(storageService.getByMaterial(material));
    }

    @GetMapping("{productCode}/getCode")
    public ResponseEntity<List<Storage>> getStorageByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Received GET by product code request for storage.");

        return  ResponseEntity.ok().body(storageService.getByProductCode(productCode));
    }

    @GetMapping("/{productId}/get")
    public ResponseEntity<Storage> getStorageByProductId(@PathVariable("productId") Long productId) throws EntityNotFoundException {
        log.info("Received GET by product id request for storage.");

        return ResponseEntity.ok().body(storageService.getStorageByProductId(productId));
    }
}
