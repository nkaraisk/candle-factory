package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    private Customer customer;

    @NotNull
    private LocalDate date;

    @ManyToOne(optional = false)
    private Product productType;

    @Positive
    private double quantity;

    private double cost;

    public double calculateCost() {
        return quantity * productType.getPrice();
    }
}
