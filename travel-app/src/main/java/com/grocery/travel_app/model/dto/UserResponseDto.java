package com.grocery.travel_app.model.dto;

import com.grocery.travel_app.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private Role role;
}
