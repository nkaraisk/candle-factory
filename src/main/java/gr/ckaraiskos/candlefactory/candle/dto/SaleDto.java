package gr.ckaraiskos.candlefactory.candle.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaleDto {
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long productId;

    @NotNull
    private Double quantity;

    private LocalDate date;

    private BigDecimal totalCost;
}
