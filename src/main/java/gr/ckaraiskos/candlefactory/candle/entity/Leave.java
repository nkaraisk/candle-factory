package gr.ckaraiskos.candlefactory.candle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "worker_leaves") // <--- ΠΡΟΣΘΗΚΗ ΑΥΤΟΥ (Για να αποφύγουμε το reserved keyword)
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

    @NotNull
    @ManyToOne(optional = false)
    private Worker worker;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public int calculateLeaveDays() {
        // Το +1 χρειάζεται επειδή η between είναι "exclusive" στο τέλος.
        // Π.χ. Δευτέρα με Δευτέρα = 0 διαφορά, αλλά 1 μέρα άδειας.
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return (int) days;
    }

}
