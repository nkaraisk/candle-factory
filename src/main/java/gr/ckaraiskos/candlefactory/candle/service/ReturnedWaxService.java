package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.dto.ReturnedWaxDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.ReturnedWax;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.repository.CustomerRepository;
import gr.ckaraiskos.candlefactory.candle.repository.ReturnedWaxRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReturnedWaxService {

    private static final Logger log = LoggerFactory.getLogger(ReturnedWaxService.class);

    private final ReturnedWaxRepository returnedWaxRepository;
    private final CustomerRepository customerRepository;
    private static final BigDecimal PURE_PRICE = BigDecimal.valueOf(3.2d);
    private static final BigDecimal NON_PURE_PRICE = BigDecimal.valueOf(0.7d);

    public ResponseEntity<ReturnedWax> create(ReturnedWaxDto dto) {
        log.info("Recording returned wax for customer {}", dto.getCustomerId());
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("No customer with id " + dto.getCustomerId()));

        ReturnedWax returnedWax = new ReturnedWax();
        returnedWax.setCustomer(customer);
        returnedWax.setReturnDate(dto.getReturnDate() != null ? dto.getReturnDate() : LocalDate.now());
        returnedWax.setMaterial(dto.getMaterial());
        returnedWax.setWeight(dto.getWeight());
        returnedWax.setNote(dto.getNote());

        BigDecimal valuePerKg = returnedWax.getMaterial() == Product.materialType.Pure ? PURE_PRICE : NON_PURE_PRICE;
        returnedWax.setTotalValue(BigDecimal.valueOf(dto.getWeight()).multiply(valuePerKg));

        returnedWaxRepository.save(returnedWax);
        return ResponseEntity.ok(returnedWax);
    }

    public ResponseEntity<List<ReturnedWax>> getAll() {
        return ResponseEntity.ok(returnedWaxRepository.findAll());
    }

    public ResponseEntity<Void> delete(Long returnedWaxId) {
        log.info("Deleting returned wax {}", returnedWaxId);
        if (!returnedWaxRepository.existsById(returnedWaxId)) {
            throw new EntityNotFoundException("No returned wax entry with id " + returnedWaxId);
        }
        returnedWaxRepository.deleteById(returnedWaxId);
        return ResponseEntity.noContent().build();
    }
}
