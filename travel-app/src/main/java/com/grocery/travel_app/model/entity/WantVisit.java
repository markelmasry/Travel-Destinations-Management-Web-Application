package com.grocery.travel_app.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "want_visits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WantVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;
}
