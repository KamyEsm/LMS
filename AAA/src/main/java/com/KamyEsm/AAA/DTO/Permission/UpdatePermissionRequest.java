package com.KamyEsm.AAA.DTO.Permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdatePermissionRequest {
    @Size(min = 2 , max = 30)
    private String name;
    private String description;
}
