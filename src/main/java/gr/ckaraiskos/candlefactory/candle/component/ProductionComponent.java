package gr.ckaraiskos.candlefactory.candle.component;

import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.repository.ProductionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductionComponent {

    private final ProductionRepository productionRepository;
    private final StorageComponent storageComponent;
    private final ProductComponent productComponent;

    @Transactional
    public Production tryAddProduction(ProductionDto productionDto) throws EntityAlreadyExistsException, EntityNotFoundException {
        log.info("Trying to add production.");

        Product product = productComponent.tryFindProduct(productionDto.getProductId());

        log.info("Checking if production already exists.");
        Optional<Production> production = productionRepository.findByDateOfProductionAndProduct(productionDto.getDate(), product);
        if (production.isPresent()) {
            log.info("Production already exists.");
            throw new EntityAlreadyExistsException("Production already exists");
        }

        Production newProduction = Production.builder()
                .dateOfProduction(productionDto.getDate())
                .product(product)
                .quantity(productionDto.getQuantity())
                .build();

        log.info("Saving new production.");
        productionRepository.save(newProduction);

        //Storage Update
        Storage storage = storageComponent.tryGetStorageByProductId(productionDto.getProductId());
        storageComponent.tryUpdateStorage(storage.getId(), storage.getQuantity() + productionDto.getQuantity());
        log.info("Added production successfully.");

        return newProduction;
    }

    @Transactional
    public Production tryUpdateProduction(ProductionDto productionDto) throws EntityNotFoundException {
        log.info("Trying to update production.");

        Production production = productionRepository.findById(productionDto.getId())
                .orElseThrow(() -> {
                    log.info("Production not found.");
                    return new EntityNotFoundException("Production not found");
                });
        log.info("Successfully retrieved production. Updating...");

        if(!production.getProduct().getId().equals(productionDto.getProductId())) {
            // Fix the previous product
            Storage oldStorage = storageComponent.tryGetStorageByProductId(production.getProduct().getId());
            storageComponent.tryUpdateStorage(oldStorage.getId(), oldStorage.getQuantity() -  production.getQuantity());

            //Fix the new product
            Storage newStorage = storageComponent.tryGetStorageByProductId(productionDto.getProductId());
            storageComponent.tryUpdateStorage(newStorage.getId(), newStorage.getQuantity() +  productionDto.getQuantity());
        }
        else if (production.getQuantity() != productionDto.getQuantity()) {
            //Fix quantity
            Storage storage = storageComponent.tryGetStorageByProductId(productionDto.getProductId());
            storageComponent.tryUpdateStorage(storage.getId(), storage.getQuantity() + (productionDto.getQuantity() - production.getQuantity()));
        }

        production.setDateOfProduction(productionDto.getDate());
        production.setProduct(productComponent.tryFindProduct(productionDto.getProductId()));
        production.setQuantity(productionDto.getQuantity());
        productionRepository.save(production);

        log.info("Updated production successfully.");
        return production;
    }

    @Transactional
    public void tryRemoveProduction(Long id) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Trying to remove production.");

        Production production = productionRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("No production found with id " + id);
                    return new EntityNotFoundException("Production not found");
                });
        log.info("Production successfully retrieved.");

        Storage storage = storageComponent.tryGetStorageByProductId(production.getProduct().getId());
        storageComponent.tryUpdateStorage(storage.getId(), storage.getQuantity() - production.getQuantity());

        productionRepository.delete(production);
        log.info("Removed production successfully.");
    }

    @Transactional(readOnly = true)
    public List<Production> tryGetAllProductions() {
        log.info("Trying to retrieve all productions.");

        List<Production> productions = productionRepository.findAll();
        log.info("Successfully retrieved all productions.");

        return productions;
    }

    @Transactional(readOnly = true)
    public List<Production> tryGetProductionByProductId(Long id) throws EntityNotFoundException {
        log.info("Trying to retrieve all productions with product id " + id);

        List<Production> productions = productionRepository.findAllByProductOrderByDateOfProductionDesc(productComponent.tryFindProduct(id));
        log.info("Successfully retrieved all productions with product id " + id);

        return productions;
    }

    @Transactional(readOnly = true)
    public List<Production> tryGetAllProductionsByDate(LocalDate date) throws EntityNotFoundException {
        log.info("Trying to retrieve all productions with date " + date);

        List<Production> productions = productionRepository.findAllByDateOfProduction(date);
        log.info("Successfully retrieved all productions with date " + date);

        return productions;
    }

    @Transactional(readOnly = true)
    public Production tryGetProductionByDateAndProductId(LocalDate date, Long productId) throws EntityNotFoundException {
        log.info("Trying to retrieve production by date and product id");

        Optional<Production> production = Optional.ofNullable(productionRepository.findByDateOfProductionAndProduct(date, productComponent.tryFindProduct(productId))
                .orElseThrow(() -> {
                    log.info("No production found with product id " + productId + "and date " + date + ".");
                    return new EntityNotFoundException("Production not found");
                }));

        log.info("Production successfully retrieved.");
        return production.get();
    }

    @Transactional(readOnly = true)
    public List<Production> tryGetAllInDateRange(LocalDate fromDate, LocalDate toDate) {
        log.info("Trying to retrieve all productions from date " + fromDate + " to date " + toDate);

        List<Production> productions = productionRepository.findAllByDateOfProductionBetween(fromDate, toDate);
        log.info("Successfully retrieved all productions from date " + fromDate + " to date " + toDate);

        return productions;
    }
}
