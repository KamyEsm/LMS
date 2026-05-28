package com.KamyEsm.AAA.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonValue
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<Role> roles;

    private String description;
}
