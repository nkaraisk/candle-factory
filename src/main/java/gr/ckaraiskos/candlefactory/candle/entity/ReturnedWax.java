package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedWax {
    static BigDecimal priceOfPure = BigDecimal.valueOf(3.2);
    static BigDecimal priceOfNonPure = BigDecimal.valueOf(0.7);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @NotNull
    private LocalDate returnDate;

    @NotNull
    @ManyToOne(optional = false)
    private Customer customer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Product.materialType material;

    @Positive
    private double weight;

    @Column(precision = 19, scale = 2)
    private BigDecimal value;

    private String note;

    private BigDecimal calculateValue() {

        BigDecimal weightBD = BigDecimal.valueOf(weight);

        // Ποσοστό == 90%
        BigDecimal retentionRate = new BigDecimal("0.90");

        // Καθαρό βάρος
        BigDecimal effectiveWeight = weightBD.multiply(retentionRate);

        BigDecimal selectedPrice;
        if (material == Product.materialType.Pure) {
            selectedPrice = priceOfPure;
        } else {
            selectedPrice = priceOfNonPure;
        }
        BigDecimal totalValue = effectiveWeight.multiply(selectedPrice);

        // Στρογγυλοποίηση
        return totalValue.setScale(2, RoundingMode.HALF_UP);
    }
}
