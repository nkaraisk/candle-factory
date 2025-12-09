package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank
    @Enumerated
    private materialType material;

    @NotBlank
    @Enumerated
    private boolean byWeight; // true αν ζυγίζεται, false αν τεμάχια

    @NotBlank
    private double price; // Τιμή ανά κιλό ή ανά τεμάχιο
}
