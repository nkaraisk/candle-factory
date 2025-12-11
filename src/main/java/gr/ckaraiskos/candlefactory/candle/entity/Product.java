package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    public enum materialType {
        Brown, White, Pure
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long Id;

    @NotBlank
    private String productCode;  // π.χ. "No1", "No3", "L40" (λαμπάδα 40εκ)

    @NotNull
    @Enumerated(EnumType.STRING)
    private materialType material;

    private boolean byWeight; // true αν ζυγίζεται, false αν τεμάχια


    @NotNull
    @PositiveOrZero
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price; // Τιμή ανά κιλό ή ανά τεμάχιο

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;
}
