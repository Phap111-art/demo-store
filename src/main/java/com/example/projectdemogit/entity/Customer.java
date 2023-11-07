package com.example.projectdemogit.entity;





import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "account_id")
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToMany(mappedBy = "customer")
    private List<Cart> carts;

    @OneToMany(mappedBy = "customer")
    private List<Comment> comments;

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

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