package com.grocery.travel_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {
    @NotBlank(message = "Password cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
