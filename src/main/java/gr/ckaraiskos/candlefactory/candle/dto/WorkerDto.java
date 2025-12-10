package gr.ckaraiskos.candlefactory.candle.dto;

import lombok.Data;

@Data
public class WorkerDto {
    private Long workerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
