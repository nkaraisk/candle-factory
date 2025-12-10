package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.CustomerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import gr.ckaraiskos.candlefactory.candle.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDto customerDto) throws EntityAlreadyExistsException{
        log.info("Requested new customer with name: {}.", customerDto.getCustomerName());

        return customerService.newCustomer(customerDto);
    }

    @PostMapping("/edit")
    public ResponseEntity<Customer> editCustomer(@RequestBody CustomerDto customerDto) throws EntityNotFoundException{
        log.info("Edit customer with id: {}.", customerDto.getCustomerId());

        return customerService.updateCustomer(customerDto);
    }

    @DeleteMapping("/{customerId}/delete")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) throws EntityNotFoundException, FailedDeletionException {
        log.info("Delete customer with id: {}.", customerId);

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() throws EntityNotFoundException{
        log.info("Get all customers.");

        return customerService.allCustomers();
    }

    @GetMapping("/name")
    public ResponseEntity<Customer> getCustomerByName(@RequestParam String customerName) throws EntityNotFoundException{
        log.info("Get customer by name: {}.", customerName);

        return customerService.getCustomerName(customerName);
    }

    @GetMapping("/phone")
    public ResponseEntity<Customer> getCustomerByPhone(@RequestParam String customerPhone) {
        log.info("Get customer by phone number: {}.", customerPhone);

        return  customerService.getCustomerPhone(customerPhone);
    }
}
