package com.abdelaziz26.metriplate.entities.store;

import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Table(name = "order_items")
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}
