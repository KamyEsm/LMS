package com.KamyEsm.AAA.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonValue
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<MyUser> Users;

    @ManyToMany(cascade = {CascadeType.MERGE , CascadeType.DETACH , CascadeType.REFRESH , CascadeType.PERSIST})
    @JoinTable(
            name = "permission_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    //owner
    private Set<Permission> permissions;
}
