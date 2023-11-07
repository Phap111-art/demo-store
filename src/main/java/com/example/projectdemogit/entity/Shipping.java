package com.example.projectdemogit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shippings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shippingId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_method")
    private String shippingMethod;

    /*---------------------------------*/

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @PrePersist
    protected void persistEntity() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void updateEntity() {
        updatedAt = LocalDateTime.now();
    }

}