package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.dto.WorkerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Worker;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.repository.LeaveRepository;
import gr.ckaraiskos.candlefactory.candle.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final LeaveRepository leaveRepository;

    public Worker createWorker(WorkerDto newWorkerDto) throws EntityAlreadyExistsException {

        Optional<Worker> possibleNewWorker = workerRepository.findWorkerByFirstNameAndLastNameAndPhoneNumber(newWorkerDto.getFirstName(), newWorkerDto.getLastName(), newWorkerDto.getPhoneNumber());

        if (possibleNewWorker.isPresent()) {
            log.error("Worker already exists");
            throw new EntityAlreadyExistsException(newWorkerDto.getFirstName() + " " + newWorkerDto.getLastName() + " already exists!");
        }
        else {
            Worker newWorker = Worker.builder().firstName(newWorkerDto.getFirstName())
                    .lastName(newWorkerDto.getLastName())
                    .phoneNumber(newWorkerDto.getPhoneNumber())
                    .build();

            workerRepository.save(newWorker);

            return newWorker;
        }
    }

    public ResponseEntity<List<Worker>> getAllWorkers() throws EntityNotFoundException {
        log.info("Getting all workers");

        List<Worker> workers = workerRepository.findAll();

        if (workers.isEmpty()) {
            log.error("No workers found");
            throw new EntityNotFoundException("No Workers found!");
        }

        return ResponseEntity.ok().body(workers);
    }

    public ResponseEntity<List<Worker>> getWorkerByFullName(WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Getting worker by full name");

        List<Worker> workers = workerRepository.findWorkerByFirstNameAndLastName(workerDto.getFirstName(), workerDto.getLastName());

        if (workers.isEmpty()) {
            log.error("No Workers found with name: {} {}.!",  workerDto.getFirstName(), workerDto.getLastName());
            throw new EntityNotFoundException("No Workers found with name " + workerDto.getFirstName() + " " + workerDto.getLastName() + ".");
        }

        return ResponseEntity.ok().body(workers);
    }

    public ResponseEntity<List<Worker>> getWorkerByFirstName(WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Getting worker by first name");

        List<Worker> workers = workerRepository.findWorkerByFirstName(workerDto.getFirstName());

        if (workers.isEmpty()) {
            log.error("No Workers found with first name {}.", workerDto.getFirstName());
            throw new EntityNotFoundException("No Workers found with first name: " + workerDto.getFirstName() + ".");
        }

        return ResponseEntity.ok().body(workers);
    }

    public ResponseEntity<List<Worker>> getWorkerByLastName(WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Getting worker by last name");

        List<Worker> workers = workerRepository.findWorkerByLastName(workerDto.getLastName());

        if (workers.isEmpty()) {
            log.error("No Workers found with last name: {}.", workerDto.getLastName());
            throw new EntityNotFoundException("No Workers found with last name: " + workerDto.getLastName() + ".");
        }

        return ResponseEntity.ok().body(workers);
    }

    public ResponseEntity<Worker> getWorkerByPhoneNumber(WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Getting worker by phone number");

        Optional<Worker> worker = workerRepository.findWorkerByPhoneNumber(workerDto.getPhoneNumber());

        if (worker.isPresent()) {
            return ResponseEntity.ok().body(worker.get());
        }
        else {
            log.error("No Worker found with phone number {}.", workerDto.getPhoneNumber());
            throw new EntityNotFoundException("No Worker found with phone number: " + workerDto.getPhoneNumber() + ".");
        }
    }

    public ResponseEntity<Worker> editWorker(Long workerId, WorkerDto newData) throws EntityNotFoundException {
        log.info("Editing worker");

        Optional<Worker> existingWorker = workerRepository.findWorkerById(workerId);
        if (existingWorker.isPresent()) {
            Worker updatedWorker = Worker.builder()
                    .id(workerId)
                    .firstName(newData.getFirstName())
                    .lastName(newData.getLastName())
                    .phoneNumber(newData.getPhoneNumber())
                    .build();

            workerRepository.save(updatedWorker);

            log.info("Worker updated successfully!");
            return  ResponseEntity.ok().body(updatedWorker);
        }

        log.error("Worker with id {} failed to edit!", workerId);
        throw new EntityNotFoundException("No Worker found with id: " + workerId + ".");
    }


    @Transactional
    public ResponseEntity<Void> deleteWorker(Long workerId) {

        log.info("Deleting all leaves of worker with id {}.", workerId);
        leaveRepository.deleteByWorkerId(workerId);

        log.info("Deleting worker.");
        workerRepository.deleteWorkerById(workerId);

        log.info("Checking deletion.");
        Optional<Worker> deletionCheck = workerRepository.findWorkerById(workerId);
        if (deletionCheck.isPresent()) {
            log.error("Failed to delete worker with id: {}.", workerId);
            return ResponseEntity.badRequest().build();
        }

        log.info("Successfully deleted worker with id: {}.", workerId);
        return ResponseEntity.noContent().build();
    }
}
