package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.ProductionComponent;
import gr.ckaraiskos.candlefactory.candle.dto.ProductionDto;
import gr.ckaraiskos.candlefactory.candle.entity.Production;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductionComponent productionComponent;

    public Production newProduction(ProductionDto productionDto) throws EntityNotFoundException, EntityAlreadyExistsException {
        log.info("Starting add production procedure");

        return productionComponent.tryAddProduction(productionDto);
    }

    public Production updateProduction(ProductionDto productionDto) throws EntityNotFoundException {
        log.info("Starting update production procedure");

        return productionComponent.tryUpdateProduction(productionDto);
    }

    public void removeProduction(Long id) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Starting remove production procedure");

        productionComponent.tryRemoveProduction(id);
    }

    public List<Production> getAll() {
        log.info("Starting getAll production procedure");

        return productionComponent.tryGetAllProductions();
    }

    public List<Production> getByProductId(Long productId) throws EntityNotFoundException {
        log.info("Starting getByProductId production procedure");

        return productionComponent.tryGetProductionByProductId(productId);
    }

    public List<Production> getByDate(LocalDate date) {
        log.info("Starting getByDate production procedure");

        return productionComponent.tryGetAllProductionsByDate(date);
    }

    public Production getByDateAndProductId(LocalDate date, Long productId) throws EntityNotFoundException {
        log.info("Starting getByDateAndProductId production procedure");

        return productionComponent.tryGetProductionByDateAndProductId(date, productId);
    }

    public List<Production> getByDateRange(LocalDate fromDate, LocalDate toDate) {
        log.info("Starting getByDateRange production procedure");

        return productionComponent.tryGetAllInDateRange(fromDate, toDate);
    }
}
