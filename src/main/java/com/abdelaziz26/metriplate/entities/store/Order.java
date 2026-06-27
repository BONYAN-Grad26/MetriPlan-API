package com.abdelaziz26.metriplate.entities.store;

import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.enums.store.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems;

    private BigDecimal totalPrice;

    private LocalDateTime orderedAt = LocalDateTime.now();

    private LocalDateTime deliveredAt;

    private OrderStatus orderStatus;
}
