package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.SaleComponent;
import gr.ckaraiskos.candlefactory.candle.dto.SaleDto;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.StorageViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleComponent saleComponent;

    public Sale newSale(SaleDto saleDto) throws EntityAlreadyExistsException, EntityNotFoundException, StorageViolationException {
        log.info("Start adding procedure.");

        return saleComponent.tryAddSale(saleDto);
    }

    public Sale updateSale(SaleDto saleDto) throws EntityNotFoundException, StorageViolationException {
        log.info("Start update procedure.");

        return saleComponent.tryUpdateSale(saleDto);
    }

    public void deleteSaleById(Long saleId) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Start delete procedure.");

        saleComponent.tryDeleteSale(saleId);
    }

    public List<Sale> getAll() {
        log.info("Start getAll sales procedure.");

        return saleComponent.tryGetAllSales();
    }

    public List<Sale> getAllByCustomerId(Long customerId) throws EntityNotFoundException {
        log.info("Start getAllByCustomerId procedure.");

        return saleComponent.tryGetAllSalesByCustomerId(customerId);
    }

    public List<Sale> getAllByProductId(Long productId) throws EntityNotFoundException {
        log.info("Start getAllByProductId procedure.");

        return saleComponent.tryGetAllSalesByProductId(productId);
    }

    public List<Sale> getAllByDate(LocalDate date) throws EntityNotFoundException {
        log.info("Start getAllByDate procedure.");

        return saleComponent.tryGetAllSalesByDate(date);
    }

    public List<Sale> getAllByCustomerIdAndProductId(Long customerId, Long productId) throws EntityNotFoundException {
        log.info("Start getAllByCustomerIdAndProductId procedure.");

        return saleComponent.tryGetAllSalesByCustomerIdAndProductId(customerId, productId);
    }

    public List<Sale> getAllByCustomerIdAndDate(Long customerId, LocalDate date) throws EntityNotFoundException {
        log.info("Start getAllByCustomerIdAndDate procedure.");

        return saleComponent.tryGetAllSalesByDateAndCustomerId(date, customerId);
    }

    public List<Sale> getAllByProductIdAndDate(Long productId, LocalDate date) throws EntityNotFoundException {
        log.info("Start getAllByProductIdAndDate procedure.");

        return saleComponent.tryGetAllSalesByDateAndProductId(date, productId);
    }

    public List<Sale> getByAll(Long customerId, Long productId, LocalDate date) throws EntityNotFoundException {
        log.info("Start getByAll procedure.");

        return saleComponent.tryGetByAll(customerId, productId, date);
    }
}
