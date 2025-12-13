package gr.ckaraiskos.candlefactory.candle.component;

import gr.ckaraiskos.candlefactory.candle.dto.SaleDto;
import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleComponent {

    private final SaleRepository saleRepository;
    private final ProductComponent productComponent;
    private final StorageComponent storageComponent;
    private final CustomerComponent customerComponent;

    @Transactional
    public Sale tryAddSale(SaleDto saleDto) throws EntityAlreadyExistsException, EntityNotFoundException {
        log.info("Trying to add sale.");

        Product product = productComponent.tryFindProduct(saleDto.getProductId());
        Customer customer = customerComponent.tryFindCustomer(saleDto.getCustomerId());

        Sale newSale = Sale.builder()
                .date(saleDto.getDate())
                .customer(customer)
                .productType(product)
                .quantity(saleDto.getQuantity())
                .build();

        log.info("Calculating cost.");
        if (saleDto.getTotalCost() != null) {
            newSale.setCost(saleDto.getTotalCost());
        }
        else {
            BigDecimal cost = calculateCost(product.getPrice(), newSale.getQuantity());
            newSale.setCost(cost);
        }

        log.info("Updating debt.");
        customerComponent.modifyDebt(newSale.getCost(), customer.getId());

        log.info("Updating Storage.");
        Storage storage = storageComponent.tryGetStorageByProductId(saleDto.getProductId());
        storageComponent.tryUpdateStorage(storage.getId(), storage.getQuantity() - saleDto.getQuantity());

        log.info("Saving sale.");
        saleRepository.save(newSale);

        log.info("Sale added successfully.");
        return newSale;
    }

    @Transactional
    public Sale tryUpdateSale(SaleDto saleDto) throws EntityNotFoundException {
        log.info("Trying to update sale.");

        Sale sale = saleRepository.findById(saleDto.getId())
                .orElseThrow(() -> {
                    log.info("Sale with id {} not found.", saleDto.getId());
                    return new EntityNotFoundException("Sale not found.");
                });
        log.info("Successfully retrieved sale.");

        // Επιστρέφουμε τα λεφτά στον παλιό πελάτη (μείωση χρέους)
        // Προσοχή: Χρησιμοποιούμε negate() για να αφαιρέσουμε το ποσό που είχε χρεωθεί
        customerComponent.modifyDebt(sale.getCost().negate(), sale.getCustomer().getId());

        // Επιστρέφουμε το εμπόρευμα στην αποθήκη
        Storage oldStorage = storageComponent.tryGetStorageByProductId(sale.getProductType().getId());
        storageComponent.tryUpdateStorage(oldStorage.getId(), oldStorage.getQuantity() + sale.getQuantity());


        // --- ΕΝΗΜΕΡΩΣΗ ΑΝΤΙΚΕΙΜΕΝΟΥ ---
        Customer newCustomer = customerComponent.tryFindCustomer(saleDto.getCustomerId());
        Product newProduct = productComponent.tryFindProduct(saleDto.getProductId());

        // Υπολογισμός νέου κόστους (αν δεν δόθηκε χειροκίνητα)
        BigDecimal finalCost;
        if (saleDto.getTotalCost() != null) {
            finalCost = saleDto.getTotalCost();
        } else {
            finalCost = calculateCost(newProduct.getPrice(), saleDto.getQuantity());
        }

        sale.setDate(saleDto.getDate());
        sale.setCustomer(newCustomer);
        sale.setProductType(newProduct);
        sale.setQuantity(saleDto.getQuantity()); // Διόρθωση: saleDto.Quantity
        sale.setCost(finalCost); // Διόρθωση: finalCost (όχι null)

        // Χρεώνουμε τον νέο πελάτη (μπορεί να είναι ο ίδιος, δεν πειράζει)
        customerComponent.modifyDebt(finalCost, newCustomer.getId());

        // Αφαιρούμε το εμπόρευμα από τη νέα αποθήκη (μείωση στοκ)
        // Προσοχή: Αν το προϊόν είναι ίδιο, το oldStorage έχει ήδη ενημερωθεί στην αρχική κατάσταση,
        // οπότε τραβάμε φρέσκα δεδομένα για σιγουριά.
        Storage targetStorage = storageComponent.tryGetStorageByProductId(newProduct.getId());
        storageComponent.tryUpdateStorage(targetStorage.getId(), targetStorage.getQuantity() - saleDto.getQuantity());

        // 5. Αποθήκευση
        saleRepository.save(sale);

        log.info("Sale updated successfully.");
        return sale;
    }

    @Transactional
    public void tryDeleteSale(Long saleId) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Trying to delete sale.");

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> {
                    log.info("Sale with id {} not found.", saleId);
                    return new EntityNotFoundException("Sale not found.");
                });
        log.info("Successfully retrieved sale.");

        // Επιστρέφουμε τα λεφτά στον παλιό πελάτη (μείωση χρέους)
        // Προσοχή: Χρησιμοποιούμε negate() για να αφαιρέσουμε το ποσό που είχε χρεωθεί
        customerComponent.modifyDebt(sale.getCost().negate(), sale.getCustomer().getId());

        // Επιστρέφουμε το εμπόρευμα στην αποθήκη
        Storage oldStorage = storageComponent.tryGetStorageByProductId(sale.getProductType().getId());
        storageComponent.tryUpdateStorage(oldStorage.getId(), oldStorage.getQuantity() + sale.getQuantity());
        log.info("Reverted changes in customer and storage.");

        log.info("Deleting sale.");
        saleRepository.delete(sale);
        log.info("Sale deleted successfully.");
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSales() {
        log.info("Trying to retrieve all sales.");

        List<Sale> sales = saleRepository.findAll();
        log.info("Successfully retrieved sales.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByCustomerId(Long customerId) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by customer id.");

        Customer customer = customerComponent.tryFindCustomer(customerId);
        List<Sale> sales = saleRepository.findAllByCustomerOrderByDate(customer);
        log.info("Successfully retrieved sales by customer id.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByProductId(Long productId) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by product id.");

        Product product = productComponent.tryFindProduct(productId);
        List<Sale> sales = saleRepository.findAllByProductTypeOrderByDate(product);
        log.info("Successfully retrieved sales by product id.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByDate(LocalDate date) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by date.");

        List<Sale> sales = saleRepository.getAllByDate(date);
        log.info("Successfully retrieved sales by date.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByCustomerIdAndProductId(Long customerId, Long productId) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by customer and product id.");

        Customer customer = customerComponent.tryFindCustomer(customerId);
        Product product = productComponent.tryFindProduct(productId);
        List<Sale> sales = saleRepository.findAllByCustomerAndProductType(customer, product);
        log.info("Successfully retrieved sales by customer and product id.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByDateAndCustomerId(LocalDate date, Long customerId) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by customer and date.");

        Customer customer = customerComponent.tryFindCustomer(customerId);
        List<Sale> sales = saleRepository.findAllByCustomerAndDate(customer, date);
        log.info("Successfully retrieved sales by customer and date.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetAllSalesByDateAndProductId(LocalDate date, Long productId) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by date and product id.");

        Product product = productComponent.tryFindProduct(productId);
        List<Sale> sales = saleRepository.getAllByProductTypeAndDate(product, date);
        log.info("Successfully retrieved sales by date and product id.");

        return sales;
    }

    @Transactional(readOnly = true)
    public List<Sale> tryGetByAll(Long customerId, Long productId, LocalDate date) throws EntityNotFoundException {
        log.info("Trying to retrieve all sales by customer, product id and date.");

        Customer customer = customerComponent.tryFindCustomer(customerId);
        Product product = productComponent.tryFindProduct(productId);
        List<Sale> sales = saleRepository.findAllByCustomerAndProductTypeAndDate(customer, product, date);
        log.info("Successfully retrieved sales by customer, product id and date.");

        return sales;
    }

    public BigDecimal calculateCost(BigDecimal unitPrice, double quantity) {
        BigDecimal weightBD = BigDecimal.valueOf(quantity);
        return weightBD.multiply(unitPrice);
    }
}
