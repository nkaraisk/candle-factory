package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.CustomerComponent;
import gr.ckaraiskos.candlefactory.candle.dto.CustomerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerComponent customerComponent;

    public ResponseEntity<Customer> newCustomer(CustomerDto customerDto) throws EntityAlreadyExistsException {
        log.info("Creating new customer.");

        Customer customer = customerComponent.tryAddCustomer(customerDto);
        log.info("New customer added successfully.");

        return ResponseEntity.ok().body(customer);
    }

    public ResponseEntity<Customer> updateCustomer(CustomerDto customerDto) throws EntityNotFoundException {
        log.info("Updating customer with id: {}.", customerDto.getCustomerId());

        return  ResponseEntity.ok().body(customerComponent.tryUpdateCustomer(customerDto));
    }

    public ResponseEntity<Void> deleteCustomer(Long customerId) throws EntityNotFoundException, FailedDeletionException {
        log.info("Deleting customer with id: {}.", customerId);
        customerComponent.tryDeleteCustomer(customerId);

        return  ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<Customer>> allCustomers() throws EntityNotFoundException {
        log.info("Retrieving all customers.");

        return ResponseEntity.ok().body(customerComponent.tryGetAll());
    }

    public ResponseEntity<Customer> getCustomerName(String customerName) throws EntityNotFoundException {
        log.info("Retrieving customer with name: {}.", customerName);

        return ResponseEntity.ok().body(customerComponent.tryGetCustomerByName(customerName));
    }

    public ResponseEntity<Customer> getCustomerPhone(String customerPhone) throws EntityNotFoundException {
        log.info("Retrieving customer with phone number: {}.", customerPhone);

        return ResponseEntity.ok().body(customerComponent.tryGetCustomerByPhone(customerPhone));
    }
}
