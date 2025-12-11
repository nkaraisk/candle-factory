package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long Id;

    @NotNull
    private LocalDate dateOfProduction;

    @NotNull
    @ManyToOne(optional = false)
    private Product product;

    @Positive
    private double quantity;// κιλά ή τεμάχια, ανάλογα productType.byWeight

}
