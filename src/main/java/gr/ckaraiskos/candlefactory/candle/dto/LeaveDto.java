package gr.ckaraiskos.candlefactory.candle.dto;

import gr.ckaraiskos.candlefactory.candle.entity.Worker;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDto {

    private Long leaveId;
    private Long workerId;
    private LocalDate startDate;
    private LocalDate endDate;
}
