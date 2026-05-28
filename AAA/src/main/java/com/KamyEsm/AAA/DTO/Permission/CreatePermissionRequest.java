package com.KamyEsm.AAA.DTO.Permission;

import com.KamyEsm.AAA.Entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreatePermissionRequest {
    @Size(min = 2 , max = 30)
    @NotBlank
    private String name;
    private String description;
}
