package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private LocalDate dateOfProduction;

    @NotBlank
    @ManyToOne(optional = false)
    private Product product;

    @NotBlank
    private double quantity;// κιλά ή τεμάχια, ανάλογα productType.byWeight
}
