package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.component.StorageComponent;
import gr.ckaraiskos.candlefactory.candle.dto.StorageDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageComponent storageComponent;

    public Storage newStorage(StorageDto storageDto) throws EntityAlreadyExistsException {
        log.info("Start adding procedure.");

        return storageComponent.tryAddStorage(storageDto);
    }

    public Storage updateStorage(Long id, double quantity) throws EntityNotFoundException {
        log.info("Start edit procedure.");

        return  storageComponent.tryUpdateStorage(id, quantity);
    }

    public void deleteStorage(Long id) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Start delete procedure.");

        storageComponent.tryDeleteStorage(id);
    }

    public List<Storage> getAll() {
        log.info("Start getting all storages procedure.");

        return storageComponent.tryGetAllStorages();
    }

    public List<Storage> getByMaterial(Product.materialType material) {
        log.info("Start getting storages by material procedure.");

        return storageComponent.tryGetAllStoragesByMaterial(material);
    }

    public List<Storage> getByProductCode(String productCode) {
        log.info("Start getting storages by product code procedure.");

        return storageComponent.tryGetAllStoragesByProductCode(productCode);
    }

    public Storage getStorageByProductId(Long id) throws EntityNotFoundException {
        log.info("Start getting storage by product id procedure.");

        return storageComponent.tryGetStorageByProductId(id);
    }
}
