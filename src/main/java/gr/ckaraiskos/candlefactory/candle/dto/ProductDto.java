package gr.ckaraiskos.candlefactory.candle.dto;

import gr.ckaraiskos.candlefactory.candle.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long productId;

    @NotBlank(message = "Product code cannot be empty")
    private String productCode;

    @NotNull(message = "Material type is required")
    private Product.materialType material;

    private boolean byWeight;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;
}
