package com.grocery.travel_app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "destinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String country;
    private String capital;
    private String region;
    private Long population;
    private String currency;
    @Column(name = "flag_image_url",nullable = false)
    private String flagImageUrl;
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItems;
}
