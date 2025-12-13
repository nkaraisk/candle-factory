package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.CustomerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.service.CustomerService;
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
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<Customer> legacyCreate(@RequestBody CustomerDto customerDto) {
        return customerService.create(customerDto);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDto customerDto) {
        return customerService.create(customerDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> legacyGetAll() {
        return customerService.getAll();
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/name")
    public ResponseEntity<Customer> getByName(@RequestParam String customerName) {
        return customerService.getByName(customerName);
    }

    @GetMapping("/phoneNumber")
    public ResponseEntity<Customer> getByPhone(@RequestParam String customerPhone) {
        return customerService.getByPhone(customerPhone);
    }

    @PostMapping("/{customerId}/edit")
    public ResponseEntity<Customer> legacyUpdate(@PathVariable Long customerId, @RequestBody CustomerDto customerDto) {
        return customerService.update(customerId, customerDto);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable Long customerId, @RequestBody CustomerDto customerDto) {
        return customerService.update(customerId, customerDto);
    }

    @DeleteMapping("/{customerId}/delete")
    public ResponseEntity<Void> legacyDelete(@PathVariable Long customerId) {
        return customerService.delete(customerId);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        return customerService.delete(customerId);
    }
}
