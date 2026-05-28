package com.KamyEsm.AAA.DTO.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> permissions;
    private List<String> users;
}
