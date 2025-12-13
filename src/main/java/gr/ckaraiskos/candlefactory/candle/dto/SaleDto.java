package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaleDto {
    Long id;

    @NotNull
    Long customerId;

    @NotNull
    LocalDate date;

    @NotNull
    Long productId;

    @NotNull
    @Positive
    Double quantity;

    BigDecimal totalCost;
}
