package mb.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1)
    private Long id;
    private String transactionId;
    private double amount;
    private String description;
    private LocalDateTime timestamp = LocalDateTime.now();
    @ManyToOne
    private User user;

    // Getters and setters
}

