package com.rounak.impression.model; // <--- NEW PACKAGE

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "game_impressions")
@Data // Generates getters, setters, toString, equals, hashCode via Lombok
public class GameImpression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String developer;
    private String publisher;

    @Column(nullable = false)
    private String genre;

    private LocalDate releaseDate;

    @Column(nullable = false, length = 2000) // Adjust length based on expected review size
    private String impressionText;

    private Integer rating; // e.g., 1-10 or 1-5

    private String imageUrl; // URL to game cover or screenshot

    private LocalDate dateReviewed = LocalDate.now(); // Automatically set when created

    // Lombok's @Data annotation handles constructors, getters, and setters.
    // If you're not using Lombok, you'd manually generate them.
}