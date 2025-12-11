package gr.ckaraiskos.candlefactory.candle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StorageDto {
    private Long storageId;

    @NotNull
    private Long productId;

    @PositiveOrZero
    private double quantity;
}
