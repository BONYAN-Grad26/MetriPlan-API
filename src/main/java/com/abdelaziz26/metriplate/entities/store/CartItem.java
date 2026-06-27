package com.abdelaziz26.metriplate.entities.store;

import com.abdelaziz26.metriplate.entities.diet.Ingredient;
import com.abdelaziz26.metriplate.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Table(name = "cart_items")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static CartItem from(User user, Ingredient ingredient, Double quantity) {
        CartItem item = new CartItem();
        item.user = user;
        item.ingredient = ingredient;
        item.quantity = quantity;
        return item;
    }
}
