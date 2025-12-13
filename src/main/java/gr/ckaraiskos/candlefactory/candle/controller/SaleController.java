package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.SaleDto;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import gr.ckaraiskos.candlefactory.candle.service.SaleService;
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
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody SaleDto saleDto) {
        return saleService.create(saleDto);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAll() {
        return saleService.getAll();
    }

    @DeleteMapping("/{saleId}")
    public ResponseEntity<Void> delete(@PathVariable Long saleId) {
        return saleService.delete(saleId);
    }
}
