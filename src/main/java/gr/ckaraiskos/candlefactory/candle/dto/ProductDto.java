package gr.ckaraiskos.candlefactory.candle.dto;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long productId;

    @NotBlank
    private String productCode;

    @NotNull
    private Product.materialType material;

    @NotNull
    private Boolean byWeight;

    @NotNull
    private BigDecimal price;
}
