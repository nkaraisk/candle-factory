package gr.ckaraiskos.candlefactory.candle.component;


import gr.ckaraiskos.candlefactory.candle.dto.StorageDto;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Storage;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.repository.ProductRepository;
import gr.ckaraiskos.candlefactory.candle.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageComponent {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;

    @Transactional
    public Storage tryAddStorage(StorageDto storageDto) throws EntityAlreadyExistsException, EntityNotFoundException {
        log.info("Trying to add storage.");

        Product product = productRepository.findById(storageDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        log.info("Checking if storage already exists.");
        Optional<Storage> optionalStorage = storageRepository.findStorageByProduct(product);
        if (optionalStorage.isPresent()) {
            log.error("Storage already exists.");
            throw new EntityAlreadyExistsException("Storage already exists.");
        }

        Storage newStorage = Storage.builder()
                .product(product)
                .quantity(storageDto.getQuantity())
                .build();

        log.info("Saving new storage.");
        storageRepository.save(newStorage);
        log.info("Added storage successfully.");

        return newStorage;
    }


    @Transactional
    public Storage tryUpdateStorage(Long id, double quantity) throws EntityNotFoundException {
        log.info("Trying to update storage.");

        log.info("Checking if storage exists.");
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Storage not found.");
                    return new EntityNotFoundException("Storage not found.");
                });

        log.info("Successfully retrieved storage. Updating...");

        storage.setQuantity(quantity);
        storageRepository.save(storage);

        log.info("Updated storage successfully.");
        return storage;
    }


    @Transactional
    public void tryDeleteStorage(Long id) throws EntityNotFoundException, DataIntegrityViolationException {
        log.info("Trying to delete storage.");

        log.info("Checking if storage exists.");
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Storage not found.");
                    return new EntityNotFoundException("Storage not found.");
                });
        log.info("Successfully retrieved storage. Deleting...");

        storageRepository.delete(storage);
        log.info("Deleted storage successfully.");
    }


    @Transactional(readOnly = true)
    public List<Storage> tryGetAllStorages() {
        log.info("Trying to retrieve all storages.");

        List<Storage> storages = storageRepository.findAll();
        log.info("Successfully retrieved all storages.");

        return storages;
    }


    @Transactional(readOnly = true)
    public List<Storage> tryGetAllStoragesByMaterial(Product.materialType material) {
        log.info("Trying to retrieve all storages by material procedure.");

        List<Storage> storages = storageRepository.findAllByProduct_Material(material);
        log.info("Successfully retrieved all storages by material procedure.");

        return storages;
    }


    @Transactional(readOnly = true)
    public List<Storage> tryGetAllStoragesByProductCode(String productCode) {
        log.info("Trying to retrieve all storages by product code procedure.");

        List<Storage> storages = storageRepository.findAllByProduct_ProductCode(productCode);
        log.info("Successfully retrieved all storages by product code procedure.");

        return storages;
    }


    @Transactional(readOnly = true)
    public Storage tryGetStorageByProductId(Long id) throws EntityNotFoundException {
        log.info("Trying to retrieve storage by product id.");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        log.info("Checking if storage exists.");
        Optional<Storage> optionalStorage = storageRepository.findStorageByProduct(product);

        if (optionalStorage.isPresent()) {
            log.info("Successfully retrieved storage by product id.");
            return optionalStorage.get();
        }

        log.error("Storage not found.");
        throw new EntityNotFoundException("Storage not found.");
    }


    @Transactional(readOnly = true)
    public void initiateStorage(Product product) throws EntityAlreadyExistsException {
        log.info("Trying to initiate storage.");

        Optional<Storage> storage = storageRepository.findStorageByProduct(product);
        if (storage.isPresent()) {
            log.info("Storage already exists.");
            throw new EntityAlreadyExistsException("Storage already exists.");
        }

        log.info("Initiating storage...");
        Storage newStorage = Storage.builder()
                .product(product)
                .quantity(0)
                .build();

        log.info("Saving new storage.");
        storageRepository.save(newStorage);
        log.info("Added storage successfully.");
    }


    @Transactional(readOnly = true)
    public void deleteByProduct(Product product) throws EntityNotFoundException {
        log.info("Trying to delete storage by product id.");

        log.info("Checking if storage exists.");
        Optional<Storage> storage = storageRepository.findStorageByProduct(product);
        if (storage.isPresent()) {
            log.info("Successfully retrieved storage. Deleting...");

            storageRepository.delete(storage.get());
            log.info("Deleted storage successfully.");
            return;
        }

        log.error("Storage not found.");
        throw new EntityNotFoundException("Storage not found.");
    }
}
