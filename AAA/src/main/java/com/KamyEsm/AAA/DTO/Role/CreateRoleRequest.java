package com.KamyEsm.AAA.DTO.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateRoleRequest {
    @Size(min = 2, max = 30)
    @NotBlank
    private String name;
    private String description;
}
