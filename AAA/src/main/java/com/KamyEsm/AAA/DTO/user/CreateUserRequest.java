package com.KamyEsm.AAA.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @Size(min = 3 , max = 30)
    @NotBlank
    private String username;

    @Size(min = 8)
    private String password;

}
