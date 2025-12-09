package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @ManyToOne(optional = false)
    private Customer customer;

    @NotBlank
    private LocalDate date;

    @ManyToOne(optional = false)
    private Product productType;

    @NotBlank
    private double quantity;

    private double cost;

    public double calculateCost() {
        return quantity * productType.getPrice();
    }
}
