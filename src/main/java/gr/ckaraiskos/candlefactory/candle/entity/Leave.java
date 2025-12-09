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
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @NotBlank
    @ManyToOne(optional = false)
    private Worker worker;

    @NotBlank
    private LocalDate startDate;

    @NotBlank
    private LocalDate endDate;

    private int numOfLeaveDays() {
        return endDate.getDayOfYear() - startDate.getDayOfYear();
        // Να τσεκάρω τι γίνεται σε περίπτωση που πέφτει σε αλλαγή μήνα και χρόνου!!!!!!!
    }
}
