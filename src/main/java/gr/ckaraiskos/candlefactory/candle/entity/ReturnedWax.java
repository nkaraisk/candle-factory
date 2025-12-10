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
public class ReturnedWax {
    static double priceOfPure = 3.2;
    static double priceOfNonPure = 0.7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long Id;

    @NotNull
    private LocalDate returnDate;

    @NotNull
    @ManyToOne(optional = false)
    private Customer customer;

    @NotNull
    private Product.materialType material;

    @Positive
    private double weight;

    private double value;

    private String note;

    private double calculateValue() {
        if(material != Product.materialType.Pure) {
            return (weight - (weight*10/100)) * priceOfNonPure;
        }
        else {
            return (weight - (weight*10/100)) * priceOfPure;
        }
    }
}
