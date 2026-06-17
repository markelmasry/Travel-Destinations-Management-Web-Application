package com.grocery.travel_app.mapper;

import com.grocery.travel_app.model.dto.WishlistItemResponseDto;
import com.grocery.travel_app.model.entity.WishlistItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    private final UserMapper userMapper;
    private final DestinationMapper destinationMapper;

    public WishlistItemResponseDto toWishlistResponse(WishlistItem wishlist ){
        if (wishlist == null) return null;
        return WishlistItemResponseDto.builder()
                .id(wishlist.getId())
                .user(userMapper.toUserResponse(wishlist.getUser()))
                .destination(destinationMapper.toDestinationDto((wishlist.getDestination())))
                .build();
    }
    public List<WishlistItemResponseDto> toWishlistResponseList(List<WishlistItem> wishlistItems) {
        if (wishlistItems == null) {
            return null;
        }
        return wishlistItems.stream()
                .map(this::toWishlistResponse)
                .collect(Collectors.toList());
    }

}
