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
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    private int daysOfLeave;

    @Override
    public String toString() {
        return firstName + " " + lastName +  "\n"
                + "Τηλέφωνο: " + phoneNumber + "\n"
                + "'Εχει πάρει " + daysOfLeave + " ημέρες άδεια.";
    }
}
