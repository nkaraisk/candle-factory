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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long Id;

    @NotBlank
    private String name;

    private String phone;

    private double debt; // συνολικό χρέος

    private double credit; // πίστωση (απόκερο)

    private double total;

    public double calculateTotal() { return debt - credit; }
}
