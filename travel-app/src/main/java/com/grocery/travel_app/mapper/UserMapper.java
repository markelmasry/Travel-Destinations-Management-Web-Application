package com.grocery.travel_app.mapper;

import com.grocery.travel_app.model.dto.UserResponse;
import com.grocery.travel_app.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user){
        if(user==null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
