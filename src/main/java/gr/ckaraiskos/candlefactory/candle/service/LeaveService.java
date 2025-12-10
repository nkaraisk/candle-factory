package gr.ckaraiskos.candlefactory.candle.service;

import gr.ckaraiskos.candlefactory.candle.dto.LeaveDto;
import gr.ckaraiskos.candlefactory.candle.entity.Leave;
import gr.ckaraiskos.candlefactory.candle.exception.EntityNotFoundException;
import gr.ckaraiskos.candlefactory.candle.exception.LeaveComponentFailureException;
import gr.ckaraiskos.candlefactory.candle.operation.LeaveRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRules leaveRules;

    public ResponseEntity<Leave> addLeave(LeaveDto requestedLeave) throws EntityNotFoundException, LeaveComponentFailureException {
        log.info("Adding leave.");

        Leave newLeave = leaveRules.leaveAddition(requestedLeave);
        log.info("Successfully added leave.");

        return ResponseEntity.ok(newLeave);
    }

    public ResponseEntity<Leave> changeLeave(LeaveDto newLeave) throws EntityNotFoundException, LeaveComponentFailureException {
        log.info("Changing leave.");

        Leave updatedLeave = leaveRules.leaveChanges(newLeave);
        log.info("Successfully edited leave.");

        return ResponseEntity.ok(updatedLeave);
    }

    public ResponseEntity<Void> deleteLeave(LeaveDto deletingLeave) throws LeaveComponentFailureException {
        log.info("Deleting leave with id: {}.", deletingLeave.getLeaveId());

        leaveRules.leaveDeletion(deletingLeave);
        log.info("Successfully deleted leave with id: {}.", deletingLeave.getLeaveId());

        return  ResponseEntity.noContent().build();
    }
}
