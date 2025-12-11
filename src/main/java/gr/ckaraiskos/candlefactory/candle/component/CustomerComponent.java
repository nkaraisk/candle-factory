package gr.ckaraiskos.candlefactory.candle.component;

import gr.ckaraiskos.candlefactory.candle.dto.CustomerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.FailedDeletionException;
import gr.ckaraiskos.candlefactory.candle.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerComponent {

    private final CustomerRepository customerRepository;

    public Customer tryAddCustomer(CustomerDto newCustomer) throws EntityAlreadyExistsException {
        log.info("Checking customer with name:{}.", newCustomer.getCustomerName());
        Optional<Customer> customer = customerRepository.findByName(newCustomer.getCustomerName());

        if (customer.isPresent()) {
            log.error("Customer with name:{} already exists.", newCustomer.getCustomerName());
            throw new EntityAlreadyExistsException("Customer with name:" + newCustomer.getCustomerName() + " already exists.");
        }

        Customer newCustomerEntity = Customer.builder()
                .name(newCustomer.getCustomerName())
                .phoneNumber(newCustomer.getCustomerPhone())
                .build();

        log.info("Saving new customer.");
        customerRepository.save(newCustomerEntity);

        return newCustomerEntity;
    }

    public Customer tryUpdateCustomer(CustomerDto changesCustomer) throws EntityNotFoundException {
        log.info("Checking customer with Id:{}.", changesCustomer.getCustomerId());
        Optional<Customer> customer = customerRepository.findById(changesCustomer.getCustomerId());

        if (customer.isPresent()) {
            Customer updatedCustomer = Customer.builder().Id(changesCustomer.getCustomerId())
                    .name(changesCustomer.getCustomerName())
                    .phoneNumber(changesCustomer.getCustomerPhone())
                    .debt(customer.get().getDebt())
                    .build();

            log.info("Updating customer to Database.");
            customerRepository.save(updatedCustomer);

            log.info("Successfully updated customer.");
            return updatedCustomer;
        }

        log.error("Customer with Id:{} does not exist.", changesCustomer.getCustomerId());
        throw new EntityNotFoundException("Customer with Id:" + changesCustomer.getCustomerId() + " does not exist.");
    }

    public void tryDeleteCustomer(Long customerId) throws EntityNotFoundException, FailedDeletionException {
        log.info("Searching customer with Id:{}.", customerId);
        customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer with Id:{} does not exist.", customerId);
                    return new EntityNotFoundException("Customer with Id:" + customerId + " does not exist.");
                });
        log.info("Deleting customer.");
        customerRepository.deleteById(customerId);


        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        if (deletedCustomer.isPresent()) {
            log.error("Failed to delete customer with Id:{}.", customerId);
            throw new FailedDeletionException("Failed to delete customer.");
        }

        log.info("Successfully deleted customer.");
    }

    public List<Customer> tryGetAll() throws EntityNotFoundException {
        log.info("Trying to retrieve all customers.");
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            log.error("No customers found.");
            throw new EntityNotFoundException("No customers found.");
        }

        log.info("Successfully retrieved all customers.");
        return customers;
    }

    public Customer tryGetCustomerByName(String customerName) throws EntityNotFoundException {
        log.info("Trying to retrieve customer by name.");
        Optional<Customer> customer = customerRepository.findByName(customerName);

        if (customer.isPresent()) {
            log.info("Successfully retrieved customer by name.");
            return customer.get();
        }

        log.error("No customers found with name:{}.",  customerName);
        throw new EntityNotFoundException("No customers found.");
    }

    public Customer tryGetCustomerByPhone(String customerPhone) throws EntityNotFoundException {
        log.info("Trying to retrieve customer by phone number.");
        Optional<Customer> customer = customerRepository.findByPhoneNumber(customerPhone);

        if (customer.isPresent()) {
            log.info("Successfully retrieved customer by phone number.");
            return customer.get();
        }

        log.error("No customers found with phone:{}.",  customerPhone);
        throw new EntityNotFoundException("No customers found.");
    }

    @Transactional
    public BigDecimal modifyDebt(BigDecimal amount, Long customerId) throws EntityNotFoundException {
        log.info("Finding customer with Id:{}.", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer with Id:{} could not be found.", customerId);
                    return new EntityNotFoundException("Customer with Id:" + customerId + " not found.");
                });

        log.info("Successfully retrieved customer. Adding {} to debt.", amount);

        //Αν το χρέος είναι null, το θεωρούμε 0
        BigDecimal currentDebt = customer.getDebt() != null ? customer.getDebt() : BigDecimal.ZERO;

        BigDecimal newDebt = currentDebt.add(amount);
        customer.setDebt(newDebt);

        // Προαιρετικό save λόγω @Transactional
        customerRepository.save(customer);

        log.info("Successfully updated customer debt to {}.", newDebt);

        return newDebt;
    }
}
