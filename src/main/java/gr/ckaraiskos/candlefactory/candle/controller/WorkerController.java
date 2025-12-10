package gr.ckaraiskos.candlefactory.candle.controller;


import gr.ckaraiskos.candlefactory.candle.dto.WorkerDto;
import gr.ckaraiskos.candlefactory.candle.entity.Worker;
import gr.ckaraiskos.candlefactory.candle.exception.EntityAlreadyExistsException;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/worker") // Prefix all routes in this controller with /api / localhost://8080/api
public class WorkerController {

    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Worker> addWorker(@RequestBody WorkerDto newWorkerRequest) throws EntityAlreadyExistsException {
        log.info("Register a new Worker");

        return workerService.createWorker(newWorkerRequest);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Worker>> getAllWorkers() throws EntityNotFoundException {
        log.info("Get all workers");

        return workerService.getAllWorkers();
    }

    @GetMapping("/fullName")
    public ResponseEntity<List<Worker>> getWorkerByFullName(@RequestBody WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Get worker by full name");

        return workerService.getWorkerByFullName(workerDto);
    }

    @GetMapping("/firsName")
    public ResponseEntity<List<Worker>> getWorkerByName(@RequestBody WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Get worker by name");

        return workerService.getWorkerByFirstName(workerDto);
    }

    @GetMapping("/surname")
    public ResponseEntity<List<Worker>> getWorkerBySurname(@RequestBody WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Get worker by surname");

        return workerService.getWorkerByLastName(workerDto);
    }

    @GetMapping("/phoneNumber")
    public ResponseEntity<Worker> getWorkerByPhoneNumber(@RequestBody WorkerDto workerDto) throws EntityNotFoundException {
        log.info("Get worker by phone number");

        return workerService.getWorkerByPhoneNumber(workerDto);
    }

    @PostMapping("/{workerId}/edit")
    public ResponseEntity<Worker> editWorker(@PathVariable("workerId") Long workerId, @RequestBody WorkerDto newData) {
        log.info("Edit worker");

        return workerService.editWorker(workerId, newData);
    }

    @DeleteMapping("/{workerId}/delete")
    public ResponseEntity<Void> deleteWorker(@PathVariable("workerId") Long workerId) {
        log.info("Delete worker with id: {}.", workerId);

        return workerService.deleteWorker(workerId);
    }
}
