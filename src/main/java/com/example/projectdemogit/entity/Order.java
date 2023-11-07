package com.example.projectdemogit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToOne(mappedBy = "order")
    private Shipping shipping;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "order_notes")
    private String orderNotes;

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