package com.KamyEsm.AAA.DTO.Permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PermissionResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> roles;
}
