package gr.ckaraiskos.candlefactory.candle.dto;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnedWaxDto {
    private Long returnedWaxId;

    @NotNull
    private Long customerId;

    @NotNull
    private Double weight;

    @NotNull
    private Product.materialType material;

    private LocalDate returnDate;
    private String note;
}
