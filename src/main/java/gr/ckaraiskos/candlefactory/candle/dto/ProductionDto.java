package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductionDto {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long productId;

    @Positive
    private double quantity;
}
