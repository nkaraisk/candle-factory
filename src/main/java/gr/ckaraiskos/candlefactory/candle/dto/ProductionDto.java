package gr.ckaraiskos.candlefactory.candle.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductionDto {
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Double quantity;

    private LocalDate date;
}
