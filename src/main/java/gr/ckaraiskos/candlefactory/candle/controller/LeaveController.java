package gr.ckaraiskos.candlefactory.candle.controller;

import gr.ckaraiskos.candlefactory.candle.dto.LeaveDto;
import gr.ckaraiskos.candlefactory.candle.entity.Leave;
import gr.ckaraiskos.candlefactory.candle.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/add")
    public ResponseEntity<Leave> requestLeave(@RequestBody LeaveDto leaveDto) {
        log.info("Requesting leave for worker " + leaveDto.getWorker().toString());

        return leaveService.addLeave(leaveDto);
    }

    @PostMapping("/edit")
    public ResponseEntity<Leave> editLeave(@RequestBody LeaveDto leaveDto) {
        log.info("Edit leave with ID {}.", leaveDto.getLeaveId());

        return leaveService.changeLeave(leaveDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteLeave(@RequestBody LeaveDto leaveDto) {
        log.info("Delete leave with id: {}.", leaveDto.getLeaveId());

        return leaveService.deleteLeave(leaveDto);
    }
}
