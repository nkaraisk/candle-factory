package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.CustomerComponent;
import gr.ckaraiskos.candlefactory.candle.dto.CustomerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerComponent customerComponent;

    public ResponseEntity<Customer> create(CustomerDto dto) {
        log.info("Registering customer {}", dto.getCustomerName());
        return ResponseEntity.ok(customerComponent.tryAddCustomer(dto));
    }

    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerComponent.tryGetAll());
    }

    public ResponseEntity<Customer> getByName(String name) {
        return ResponseEntity.ok(customerComponent.tryGetCustomerByName(name));
    }

    public ResponseEntity<Customer> getByPhone(String phone) {
        return ResponseEntity.ok(customerComponent.tryGetCustomerByPhone(phone));
    }

    public ResponseEntity<Customer> update(Long customerId, CustomerDto dto) {
        log.info("Updating customer {}", customerId);
        dto.setCustomerId(customerId);
        return ResponseEntity.ok(customerComponent.tryUpdateCustomer(dto));
    }

    public ResponseEntity<Void> delete(Long customerId) {
        log.info("Deleting customer {}", customerId);
        customerComponent.tryDeleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
