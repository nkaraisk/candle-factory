package gr.ckaraiskos.candlefactory.candle.operation;

import gr.ckaraiskos.candlefactory.candle.dto.LeaveDto;
import gr.ckaraiskos.candlefactory.candle.entity.Leave;
import gr.ckaraiskos.candlefactory.candle.entity.Worker;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.LeaveComponentFailureException;
import gr.ckaraiskos.candlefactory.candle.repository.LeaveRepository;
import gr.ckaraiskos.candlefactory.candle.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveRules {

    private final LeaveRepository leaveRepository;
    private final WorkerRepository workerRepository;

    @Transactional
    public void leaveDeletion(LeaveDto deletingLeave) throws LeaveComponentFailureException {
        Optional<Leave> leave = leaveRepository.findById(deletingLeave.getLeaveId());

        //Update of leave dates for the worker
        int days = leave.get().calculateLeaveDays();
        Worker worker = leave.get().getWorker();
        int currentDays = worker.getDaysOfLeave();
        worker.setDaysOfLeave(currentDays - days);
        workerRepository.save(worker);
        //

        leaveRepository.deleteById(deletingLeave.getLeaveId());

        Optional<Leave> deletionCheck = leaveRepository.findById(deletingLeave.getLeaveId());
        if (deletionCheck.isPresent()) {
            log.error("Failed to delete leave with id {}.", deletingLeave.getLeaveId());
            throw new LeaveComponentFailureException("Failed to delete leave with id " + deletingLeave.getLeaveId());
        }
    }

    @Transactional
    protected void leaveValidation(LeaveDto requestedLeave) throws LeaveComponentFailureException {
        log.info("Checking validity of leave.");

        List<Leave> overlapping = leaveRepository.findOverlappingLeaves(requestedLeave.getStartDate(), requestedLeave.getEndDate());

        int count = overlapping.size();

        if (count == 0) {
            return; // επιτρέπεται
        }

        if (count == 1) {
            return; // επιτρέπεται -> πάμε σε "μόνο ένας" ή "όλοι"
        }

        // count >= 2 → πρέπει να ελέγξουμε αν πάμε σε "όλοι"
        Set<Long> workersOnLeave = overlapping.stream()
                .map(l -> l.getWorker().getId())
                .collect(Collectors.toSet());

        workersOnLeave.add(requestedLeave.getWorkerId()); // include the NEW one

        long totalWorkers = workerRepository.count();

        if (workersOnLeave.size() != totalWorkers) {
            throw new LeaveComponentFailureException("Leave declined: only 1 worker can be absent in the same period");
        }

        log.info("Approved leave.");
    }

    @Transactional
    public Leave leaveAddition(LeaveDto requestedLeave) throws EntityNotFoundException, LeaveComponentFailureException {

        leaveValidation(requestedLeave);

        Leave newLeave = Leave.builder()
                .startDate(requestedLeave.getStartDate())
                .endDate(requestedLeave.getEndDate())
                .build();

        //Update of leave dates for the worker
        log.info("Adding worker leave days!");
        int days = newLeave.calculateLeaveDays();
        Worker worker = workerRepository.findById(requestedLeave.getWorkerId())
                .orElseThrow(() -> new EntityNotFoundException("Worker with id " + requestedLeave.getWorkerId() + " not found."));
        int currentDays = worker.getDaysOfLeave();
        worker.setDaysOfLeave(currentDays + days);

        newLeave.setWorker(worker);
        workerRepository.save(worker);
        //

        log.info("Adding leave to Database.");
        leaveRepository.save(newLeave);

        return newLeave;
    }

    @Transactional
    public Leave leaveChanges(LeaveDto requestedLeave) throws EntityNotFoundException, LeaveComponentFailureException {

        leaveValidation(requestedLeave);

        Optional<Leave> existingLeave = leaveRepository.findById(requestedLeave.getLeaveId());
        if (existingLeave.isPresent()) {
            Leave updatedLeave = Leave.builder().id(requestedLeave.getLeaveId())
                    .startDate(requestedLeave.getStartDate())
                    .endDate(requestedLeave.getEndDate())
                    .build();

            //Update of leave dates for the worker
            log.info("Updating worker leave days!");
            int oldDays = existingLeave.get().calculateLeaveDays();
            int newDays = updatedLeave.calculateLeaveDays();
            Worker worker = existingLeave.get().getWorker();
            int currentDays = worker.getDaysOfLeave();
            worker.setDaysOfLeave(currentDays - oldDays + newDays);

            updatedLeave.setWorker(worker);
            workerRepository.save(worker);
            //

            log.info("Changing leave to Database.");
            leaveRepository.save(updatedLeave);

            log.info("Successfully changed leave to Database.");
            return updatedLeave;
        }

        log.error("Failed to change leave with id: {}.", requestedLeave.getLeaveId());
        throw new EntityNotFoundException("No leave found with id: " + requestedLeave.getLeaveId() + ".");
    }

    @Scheduled(cron = "0 0 0 1 1 ?") // 1η Ιανουαρίου τα μεσάνυχτα
    public void resetLeaveCounters() {
        List<Worker> allWorkers = workerRepository.findAll();
        for (Worker w : allWorkers) {
            w.setDaysOfLeave(0); // Μηδενισμός για το νέο έτος
        }
        workerRepository.saveAll(allWorkers);
    }
}
