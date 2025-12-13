package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDto {
    private Long customerId;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerPhone;
}
