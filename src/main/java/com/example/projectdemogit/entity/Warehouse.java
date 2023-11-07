package com.example.projectdemogit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseId;

    @Column(name = "warehouse_code")
    private String warehouseCode;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "warehouse")
    private Set<Category> categories;


}
