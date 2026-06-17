package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.ResourceNotFoundException;
import com.grocery.travel_app.mapper.WishlistMapper;
import com.grocery.travel_app.model.dto.WishlistItemResponseDto;
import com.grocery.travel_app.model.entity.Destination;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.model.entity.WishlistItem;
import com.grocery.travel_app.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final DestinationService destinationService;
    private final UserService userService;
    private final WishlistMapper wishlistMapper;

    @Transactional
    public WishlistItemResponseDto addToWishlist(Long destinationId, String username) {
        log.info(">>> Service call: Adding destination ID {} to wishlist for user: {}", destinationId, username);
        Destination destination = destinationService.getDestinationEntityById(destinationId);
        User user = userService.getUserEntityByUsername(username);
        if (wishlistRepository.existsByDestinationIdAndUserId(destinationId, user.getId())) {
            log.warn("Failed to add: Destination {} already in wishlist for {}", destinationId, username);
            throw new DuplicateResourceException("This destination is already in your bucket list.");
        }

        WishlistItem wishlistItem = WishlistItem.builder()
                .destination(destination)
                .user(user)
                .build();

        wishlistRepository.save(wishlistItem);
        log.info("Successfully added destination ID {} to wishlist for user: {}", destinationId, username);
        return wishlistMapper.toWishlistResponse(wishlistItem);
    }

    @Transactional(readOnly = true)
    public List<WishlistItemResponseDto> getUserWishlist(String username) {
        log.info(">>> Service call: Fetching wishlist items for user: {}", username);
        User user = userService.getUserEntityByUsername(username);
        List<WishlistItem> wishlistItems = wishlistRepository.findByUserId(user.getId());
        log.info("Successfully retrieved {} wishlist items for user: {}", wishlistItems.size(), username);
        return wishlistMapper.toWishlistResponseList(wishlistItems);
    }

    @Transactional
    public void removeFromWishlist(Long wishlistId, String username) {
        log.info(">>> Service call: Attempting to remove wishlist item ID {} for user: {}", wishlistId, username);
        User user = userService.getUserEntityByUsername(username);
        if (!wishlistRepository.existsByIdAndUserId(wishlistId, user.getId())) {
            log.warn("Failed to delete: Item {} not found for user {}", wishlistId, username);
            throw new ResourceNotFoundException("Wishlist item not found or unauthorized access.");
        }
        wishlistRepository.deleteByIdAndUserId(wishlistId, user.getId());
        log.info("Successfully removed wishlist item ID {} for user: {}", wishlistId, username);
    }
}