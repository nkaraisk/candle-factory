package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.ReturnedWaxDto;
import gr.ckaraiskos.candlefactory.candle.entity.ReturnedWax;
import gr.ckaraiskos.candlefactory.candle.service.ReturnedWaxService;
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
@RequestMapping("/returned-wax")
@RequiredArgsConstructor
public class ReturnedWaxController {

    private final ReturnedWaxService returnedWaxService;

    @PostMapping
    public ResponseEntity<ReturnedWax> create(@RequestBody ReturnedWaxDto returnedWaxDto) {
        return returnedWaxService.create(returnedWaxDto);
    }

    @GetMapping
    public ResponseEntity<List<ReturnedWax>> getAll() {
        return returnedWaxService.getAll();
    }

    @DeleteMapping("/{returnedWaxId}")
    public ResponseEntity<Void> delete(@PathVariable Long returnedWaxId) {
        return returnedWaxService.delete(returnedWaxId);
    }
}
