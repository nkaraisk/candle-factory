package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDto {

    private Long leaveId;

    @NotNull
    private Long workerId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
