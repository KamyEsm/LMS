package com.KamyEsm.AAA.DTO.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateUserRequest {
    @Size(min = 3 , max = 30)
    private String username;

    @Size(min = 8)
    private String password;
}
