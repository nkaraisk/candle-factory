package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WorkerDto {
    private Long workerId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;
}
