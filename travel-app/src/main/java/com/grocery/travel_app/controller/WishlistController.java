package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.WishlistItemResponseDto;
import com.grocery.travel_app.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistItemResponseDto> addToWishlist(@RequestParam Long destinationId, @RequestParam String username) {
        log.info(">>> API call: Adding destination {} to wishlist for user {}", destinationId, username);
        WishlistItemResponseDto response = wishlistService.addToWishlist(destinationId, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<WishlistItemResponseDto>> getUserWishlist(@PathVariable String username) {
        log.info(">>> API call: Fetching wishlist for user {}", username);
        return ResponseEntity.ok(wishlistService.getUserWishlist(username));
    }

    @DeleteMapping("/{visitId}/user/{username}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long visitId, @PathVariable String username) {
        log.info(">>> API call: Removing wishlist item {} for user {}", visitId, username);
        wishlistService.removeFromWishlist(visitId, username);
        return ResponseEntity.noContent().build();
    }
}