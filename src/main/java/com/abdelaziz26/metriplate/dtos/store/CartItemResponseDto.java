package com.abdelaziz26.metriplate.dtos.store;

import com.abdelaziz26.metriplate.entities.store.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.stringtemplate.v4.ST;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemResponseDto {

    Long       id;
    Long       ingredientId;
    String     ingredientName;
    String     ingredientImgUrl;
    Double     quantity;
    BigDecimal price;

    public static CartItemResponseDto from(CartItem item) {
        return CartItemResponseDto.builder()
                .ingredientId(item.getIngredient().getId())
                .quantity(item.getQuantity())
                .ingredientName(item.getIngredient().getName())
                .ingredientImgUrl(item.getIngredient().getImageUrl())
                .price(
                        item.getIngredient().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())
                                )
                )
                .build();
    }
}
