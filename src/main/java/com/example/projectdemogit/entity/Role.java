package com.example.projectdemogit.entity;


import jakarta.persistence.*;
import lombok.*;


@Builder
@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(name = "name")
    private String name;


}
